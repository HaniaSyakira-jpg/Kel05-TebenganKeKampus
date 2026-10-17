# Arsitektur — Tebengan Ke Kampus

Dokumen ini menjelaskan desain arsitektur aplikasi: lapisan Clean Architecture, alur data, dependency injection, mekanisme `expect/actual`, strategi pengujian, serta keputusan teknis beserta alasannya.

---

## 1. Prinsip Umum

Aplikasi menerapkan **MVVM** di atas **Clean Architecture** dengan **Repository Pattern**.

**Dependency Rule** — dependensi selalu mengarah ke dalam:

```
presentation ──► domain ◄── data
```

- Lapisan **domain** tidak mengetahui apa pun tentang UI maupun sumber data. Ia hanya berisi model, kontrak repositori, dan use case.
- Lapisan **data** mengimplementasikan kontrak domain.
- Lapisan **presentation** bergantung pada use case (bukan langsung ke repositori/API).

Manfaat: mudah diuji (domain & data dapat diuji tanpa UI), terpelihara, dan dapat berkembang (mis. menukar sumber data lokal menjadi REST API tanpa mengubah UI).

---

## 2. Peta Lapisan

### 2.1 Domain Layer (`domain/`)

Inti bisnis aplikasi, murni Kotlin tanpa dependensi platform.

| Berkas | Isi |
|---|---|
| `model/TitikLokasi.kt` | Titik koordinat + `jarakKmKe()` memakai **rumus Haversine** (fitur tema: Lokasi) |
| `model/Tebengan.kt` | Model utama tawaran tumpangan + `StatusTebengan` (TERBUKA/PENUH/SELESAI) |
| `model/Pengguna.kt` | Model profil pengguna |
| `repository/TebenganRepository.kt` | Interface kontrak: `amatiDaftarTebengan`, `ambilTebengan`, `tambahTebengan`, `hapusTebengan` |
| `usecase/TebenganUseCases.kt` | `AmatiDaftarTebenganUseCase`, `AmbilTebenganUseCase`, `TambahTebenganUseCase`, `HapusTebenganUseCase`, `CariTebenganTerdekatUseCase` |

`CariTebenganTerdekatUseCase` adalah jantung fitur tema: ia mengambil daftar tebengan lalu mengurutkannya menaik berdasarkan `asal.jarakKmKe(titikSaya)`.

### 2.2 Data Layer (`data/`)

| Berkas | Peran |
|---|---|
| `local/TebenganLocalDataSource.kt` | Sumber data **in-memory** berbasis `MutableStateFlow`, berisi `SampleData` (4 tawaran contoh dengan koordinat di sekitar ITERA) |
| `remote/HttpClientFactory.kt` | `expect fun createHttpClient()` + `BASE_URL` (kerangka Ktor, aktif Sprint 3) |
| `repository/TebenganRepositoryImpl.kt` | Mengimplementasikan `TebenganRepository`, kini didelegasikan ke local data source |
| `model/TebenganDto.kt` | DTO `@Serializable` + fungsi mapper `toDomain()` / `toDto()` |

DTO dan domain model **dipisah** agar perubahan bentuk data dari API tidak merembet ke seluruh aplikasi; mapper menjaga batas antar-lapisan.

> Sprint 3 akan menambahkan data source Ktor (remote) dan penyimpanan persisten (SQLDelight/DataStore), dengan `TebenganRepositoryImpl` menjadi titik orkestrasi offline-first.

### 2.3 Presentation Layer (`presentation/`)

| Direktori | Peran |
|---|---|
| `navigation/Routes.kt` + `AppNavHost.kt` | Definisi rute & `NavHost`, bottom navigation (Beranda/Peta/Profil), argument passing `detail/{id}` |
| `theme/Theme.kt` | Skema warna Material 3 (terang & gelap) bernuansa hijau ITERA |
| `components/` | Composable reusable: `TebenganCard`, `StateComponents` (loading/empty/error) |
| `screens/` | 6 layar: `home`, `detail`, `tambah`, `peta`, `profil`, `pengaturan` |

Setiap layar mengikuti pola **UDF (Unidirectional Data Flow)**:

```
ViewModel.uiState: StateFlow<UiState>  ──collectAsState──►  Composable
        ▲                                                        │
        └──────────────── user event (callback) ─────────────────┘
```

Contoh `HomeViewModel`:

```kotlin
sealed interface HomeUiState {
    data object Memuat : HomeUiState
    data class Sukses(val daftar: List<Tebengan>) : HomeUiState
    data object Kosong : HomeUiState
    data class Galat(val pesan: String) : HomeUiState
}

val uiState: StateFlow<HomeUiState> = amatiDaftarTebenganUseCase()
    .map { if (it.isEmpty()) HomeUiState.Kosong else HomeUiState.Sukses(it) }
    .catch { emit(HomeUiState.Galat(it.message ?: "Gagal memuat data")) }
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), HomeUiState.Memuat)
```

ViewModel tidak mengetahui Compose; ia hanya menyediakan `StateFlow`. UI merender state secara ekshaustif dengan `when`.

---

## 3. Dependency Injection (Koin)

Bonus Sprint 1 (+10%). Seluruh grafik dependensi didefinisikan pada satu modul: `di/AppModule.kt`.

```
TebenganLocalDataSource ──► TebenganRepositoryImpl ──► UseCases ──► ViewModels
        (single)                   (single)              (factory)     (viewModel)
```

| Definisi | Scope | Alasan |
|---|---|---|
| `TebenganLocalDataSource` | `single` | Satu sumber data dibagi seluruh aplikasi agar state konsisten |
| `TebenganRepository` → `TebenganRepositoryImpl` | `single` | Stateless, aman dibagi |
| Use case | `factory` | Ringan & tanpa state |
| ViewModel | `viewModelOf { }` | Siklus hidup dikelola lifecycle, diambil di UI via `koinViewModel()` |

Inisialisasi Koin dilakukan **satu kali per platform**, di titik masuk masing-masing:

| Platform | Titik masuk | Berkas |
|---|---|---|
| Android | `TebenganApplication.onCreate()` (via `androidContext`) | `androidMain/.../TebenganApplication.kt` |
| Desktop | `main()` sebelum `application { }` | `desktopMain/.../main.kt` |
| iOS | `MainViewController()` sebelum `ComposeUIViewController` | `iosMain/.../MainViewController.kt` |

UI bersama (`App()`) identik di ketiga platform.

---

## 4. Multiplatform: `expect`/`actual`

Logika bersama berada di `commonMain`; hal yang berbeda per platform dideklarasikan `expect` dan diimplementasikan di masing-masing `*Main`.

| `expect` (commonMain) | `actual` |
|---|---|
| `fun platformName(): String` (`util/Platform.kt`) | Android / Desktop / iOS |
| `fun createHttpClient(): HttpClient` (`data/remote/HttpClientFactory.kt`) | Android & Desktop → **OkHttp**; iOS → **Darwin** |

Target yang dibangun:

| Target | Keterangan |
|---|---|
| `androidTarget` | Wajib (minSdk 24, target/compileSdk 36) |
| `jvm("desktop")` | Untuk preview cepat & menjalankan unit test di CI tanpa emulator |
| `iosX64`, `iosArm64`, `iosSimulatorArm64` | Bonus iOS (+10%); otomatis di-skip bila host bukan macOS (`kotlin.native.ignoreDisabledTargets=true`) |

---

## 5. Strategi Pengujian

Unit test ditulis di `commonTest` sehingga dapat dijalankan baik di Desktop maupun Android.

| Berkas uji | Cakupan |
|---|---|
| `domain/model/TitikLokasiTest.kt` | Perhitungan jarak Haversine |
| `domain/usecase/CariTebenganTerdekatUseCaseTest.kt` | Urutan hasil pencarian berdasarkan jarak |
| `data/repository/TebenganRepositoryImplTest.kt` | Perilaku CRUD repositori |
| `data/model/TebenganMapperTest.kt` | Map DTO ↔ domain model |

Menjalankan:

```bash
./gradlew :composeApp:desktopTest        # JVM — paling cepat
./gradlew :composeApp:testDebugUnitTest  # Android
```

> Target Sprint 4: minimal 10 unit test + 3 UI test dengan coverage > 50%.

---

## 6. Keputusan Teknis

| Keputusan | Alasan |
|---|---|
| **Compose Multiplatform** untuk UI | Satu codebase UI untuk Android, iOS, dan Desktop |
| **Desktop target ditambahkan** | Memungkinkan menjalankan unit test dan pratinjau aplikasi di CI/Linux tanpa emulator atau macOS |
| **Version Catalog** (`libs.versions.toml`) | Versi dependensi terpusat & mudah diperbarui |
| **StateFlow + sealed UI state** | State reaktif, rendering ekshaustif, mudah diuji |
| **DTO dipisah dari domain model** | Batas lapisan yang jelas; API eksternal tidak membocorkan ke domain |
| **Data source in-memory di Sprint 1** | Aplikasi langsung dapat didemokan sebelum backend/persistensi ada |
| **Koin** | DI ringan, ramah multiplatform, sintaks DSL sederhana |
| **Haversine di domain** | Logika fitur tema dapat diuji murni tanpa perangkat/GPS |

---

## 7. Alur Data (contoh: membuka Beranda)

```
1. App() ──► AppNavHost ──► HomeScreen(koinViewModel())
2. HomeViewModel dipanggil; ia menerima AmatiDaftarTebenganUseCase (Koin)
3. UseCase ──► TebenganRepository (interface) ──► TebenganRepositoryImpl
4. RepositoryImpl ──► TebenganLocalDataSource (MutableStateFlow) / Flow<List<Tebengan>>
5. Flow dipetakan ke HomeUiState (Memuat → Sukses/Kosong/Galat)
6. HomeScreen meng-collect uiState dan merender LazyColumn berisi TebenganCard
```

Saat pengguna mengetuk salah satu kartu:

```
onLihatDetail(id) ──► navController.navigate("detail/$id")
   ──► DetailScreen membaca id dari entry.arguments ──► DetailViewModel memuat tebengan
```

---

## 8. Rencana Evolusi Arsitektur

| Sprint | Perubahan arsitektur |
|---|---|
| 2 | Lengkapi ViewModel & form; pemetaan state CRUD; penanganan error/empty |
| 3 | Tambah `RemoteDataSource` (Ktor) + `LocalDataSource` persisten (SQLDelight); `RepositoryImpl` menjadi orkestrator offline-first; data source lokasi platform (GPS + permission) |
| 4 | Test double untuk repositori; UI test Compose; penyempurnaan theme & animasi |
| 5 | Signing config, optimasi build & ukuran APK |

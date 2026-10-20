# Arsitektur — Tebengan Ke Kampus

Dokumen ini menjelaskan bagaimana kode proyek ini disusun dan kenapa kami memilih cara itu. Bagian terakhir berisi hal-hal yang kami sadari belum ideal.

## 1. Gambaran umum

Ada tiga layer, dan arah dependensinya selalu ke dalam:

```
presentation  ──▶  domain  ◀──  data
    (UI)          (aturan)     (sumber data)
```

Artinya `domain` tidak tahu apa-apa soal Compose, Android, atau dari mana datanya berasal. `data` boleh bergantung pada `domain` karena dia mengimplementasikan interface yang didefinisikan di sana. `presentation` juga bergantung pada `domain` karena memanggil use case-nya.

Ini kelihatan seperti menambah kerumitan untuk aplikasi yang masih kecil. Alasannya baru terasa saat Sprint 3: begitu data pindah dari in-memory ke SQLDelight dan ditambah REST API, yang berubah hanya di layer `data`. ViewModel dan layar tidak perlu disentuh.

## 2. Struktur folder

```
composeApp/src/
├── commonMain/kotlin/id/ac/itera/tebengankekampus/
│   ├── data/
│   │   ├── local/            penyimpanan in-memory + sumber data palsu (checkpoint 2)
│   │   ├── mapper/           ubah DTO ⇄ model domain
│   │   ├── remote/           placeholder untuk Ktor, belum dipakai
│   │   └── repository/       implementasi TebenganRepository
│   ├── domain/
│   │   ├── model/            Tebengan, TitikLokasi, PencarianLokasi, StatusPencarian
│   │   ├── repository/       interface TebenganRepository
│   │   └── usecase/          CariTebenganTerdekatUseCase, dll
│   ├── presentation/
│   │   ├── components/       TebenganCard, EmptyState, LoadingState
│   │   ├── navigation/       AppNavHost, Routes
│   │   ├── screens/          home, detail, tambah, peta, profil, pengaturan
│   │   └── theme/            warna, tipografi, Material 3 theme
│   ├── di/                   modul Koin
│   ├── platform/             expect/actual untuk HttpClient dan info platform
│   └── App.kt
├── commonTest/               unit test
├── androidMain/              MainActivity, Platform.android.kt, manifest
├── iosMain/                  MainViewController, Platform.ios.kt
└── desktopMain/              main.kt untuk jendela desktop, DemoStatusMain untuk konsol
```

## 3. Isi tiap layer

### domain

Bagian paling sederhana sekaligus paling penting, karena di sini aturan mainnya hidup.

`TitikLokasi` menyimpan latitude dan longitude, plus fungsi `jarakKmKe()` yang menghitung jarak dengan rumus Haversine. Rumus ini dipilih karena cukup akurat untuk jarak antar titik di sekitar kampus dan tidak butuh library tambahan.

`Tebengan` adalah model utama: id, nama pengemudi, kontak, titik jemput, tujuan, jadwal, jumlah kursi, harga, catatan, dan flag untuk tawaran milik sendiri.

`TebenganRepository` cuma interface. Isinya operasi yang dibutuhkan UI dan tidak lebih — `getSemuaTebengan()`, `getTebenganById()`, `tambahTebengan()`, `hapusTebengan()`.

`CariTebenganTerdekatUseCase` menerima daftar tebengan dan lokasi pengguna, lalu mengembalikan daftar yang sudah diurutkan berdasarkan jarak. Ini inti fitur tema Lokasi kami.

### data

`TebenganRepositoryImpl` mengimplementasikan interface di atas. Sekarang dia hanya membungkus `TebenganLocalDataSource`, yang menyimpan daftar di sebuah `MutableStateFlow` berisi data awal (seed) di memori.

`FakeTebenganDataSource` (Checkpoint Minggu 2) adalah sumber data palsu terpisah yang tidak dipakai aplikasi. Ia mengeluarkan status pencarian dan perubahan status tebengan lewat `Flow` dengan `delay`, untuk menunjukkan alur data yang sama seperti nantinya saat REST API masuk, sekaligus jadi bukti berjalan di konsol.

`TebenganMapper` mengubah `TebenganDto` ke model domain. Sebenarnya sekarang belum ada API, tapi DTO dan mapper tetap dibuat supaya saat Ktor masuk nanti tidak perlu mengubah bentuk data yang beredar di layer lain.

`remote/HttpClientFactory` sudah ada sebagai `expect`/`actual`, tapi belum dipanggil dari mana pun.

### presentation

ViewModel menyimpan state dan mengeluarkan `StateFlow`. Layar Compose mengumpulkannya lewat `collectAsState()`. ViewModel tidak pernah menyimpan referensi ke composable, jadi bisa diuji tanpa Android.

State UI dibuat sebagai sealed interface per layar — misalnya `HomeUiState.Loading`, `HomeUiState.Success`, `HomeUiState.Error`. Dengan begitu layar bisa melakukan `when` yang lengkap, dan compiler akan memberitahu kalau ada state yang belum ditangani.

## 4. Alur data

Contoh ketika pengguna membuka layar Peta:

```
PetaScreen
  └─ collectAsState() ◀── PetaViewModel (StateFlow)
                            └─ CariTebenganTerdekatUseCase
                                 └─ TebenganRepository (interface)
                                      └─ TebenganRepositoryImpl  [data]
                                           └─ TebenganLocalDataSource
```

Yang perlu diperhatikan: view model bergantung pada interface, bukan implementasi. Pergantian sumber data tidak menyentuh layer atas.

## 5. Dependency injection

Kami memakai Koin, dan ini bonus untuk Sprint 1. Modulnya ada di `di/` sebagai beberapa deklarasi terpisah:

- `platformModule` — `HttpClient` dan info platform
- `dataModule` — data source dan repository
- `domainModule` — use case
- `viewModelModule` — semua ViewModel

Layer `presentation` memanggil `koinViewModel()` untuk mendapatkan ViewModel-nya. Tidak ada layar yang membuat objek data secara langsung.

Alasan memilih Koin dibanding Hilt: Koin berbasis Kotlin DSL dan tidak butuh pemrosesan anotasi, jadi pengaruhnya ke waktu build hampir tidak ada, dan dia jalan di semua target KMP termasuk iOS. Hilt hanya untuk Android, jadi tidak cocok untuk proyek ini.

## 6. Kode yang berbeda per platform

Beberapa hal tidak bisa ditulis sekali untuk semua target, jadi kami pakai `expect`/`actual`:

| expect (commonMain) | Android | iOS | Desktop |
|---|---|---|---|
| `Platform` | `android.os.Build.VERSION.SDK_INT` | `UIDevice.currentDevice.systemVersion` | `System.getProperty("java.version")` |

Ini terlihat sepele, tapi justru berguna: layar Pengaturan menampilkan versi platform lewat `Platform`, dan di screenshot Android muncul "Platform: Android (API 37)". Tanpa pola ini, setiap layar harus punya tipe sendiri per platform.

iOS masih `expect/actual` saja belum diuji jalan, karena butuh Mac. Yang bisa kami pastikan sekarang hanya bahwa konfigurasinya benar dan proyeknya bisa di-build di CI.

## 7. Testing

Unit test ada di `commonTest` supaya jalan di semua target tanpa duplikasi. Yang diuji:

| File | Isi | Jumlah |
|---|---|---|
| `TitikLokasiTest` | Perhitungan Haversine untuk jarak nol dan jarak yang diketahui | 2 |
| `CariTebenganTerdekatUseCaseTest` | Hasil urutan sesuai jarak | 1 |
| `TebenganRepositoryImplTest` | Ambil semua, ambil per id (ada dan tidak ada), tambah, hapus | 4 |
| `TebenganMapperTest` | Perubahan DTO ke model domain | 3 |
| `FakeTebenganDataSourceTest` | Alur status pencarian, pengurutan jarak, perubahan status tebengan | 4 |

Total 14 test case. Semuanya berjalan tanpa emulator atau perangkat.

Target desktop ditambahkan khusus untuk ini. Tujuannya supaya `:composeApp:desktopTest` bisa jalan cepat di komputer anggota tim dan di runner Linux, tanpa menunggu emulator menyala. Di CI, kami memakai kombinasi `testDebugUnitTest` dan `desktopTest`, dan tidak memakai task `test` biasa karena task itu akan ikut menarik task iOS yang tidak mungkin jalan di Linux.

## 8. Keputusan teknis

| Keputusan | Alasan | Yang dikorbankan |
|---|---|---|
| Clean architecture 3 layer | Perubahan sumber data nanti tidak merembet ke UI | Lebih banyak file dan boilerplate |
| Compose Multiplatform | Satu UI untuk Android, iOS, desktop | Belum semua API Compose Desktop sama matangnya |
| Repository pattern | UI tidak tahu asal data; gampang di-mock saat test | Satu lapisan tambahan yang harus dilewati |
| Koin | Ringan, tidak butuh anotasi, jalan di iOS | Kesalahan dependency baru ketahuan saat aplikasi jalan, bukan saat compile |
| Haversine dihitung sendiri | Tidak menambah dependency, cukup akurat untuk skala kampus | Tidak memperhitungkan rute jalan yang sebenarnya |
| Data in-memory dulu | Memungkinkan Sprint 1 fokus ke struktur dan CI | Data hilang saat aplikasi ditutup |
| Unit test di `commonTest` | Satu set test untuk semua target | Tidak bisa menguji hal yang khusus platform |
| `expect/actual` untuk platform | Satu cara akses info platform di semua target | Dua implementasi yang harus dijaga sinkron |

## 9. Yang belum beres

Kami mencatat ini supaya tidak terlihat seperti proyek yang sudah sempurna:

1. **Ktor belum dipakai.** Dependency dan `HttpClientFactory` sudah ada, tapi belum ada pemanggilan. Ini sengaja supaya build config tidak berubah lagi di Sprint 3, tapi tetap saja ada dependency yang menganggur dua sprint.
2. **Penyimpanan masih di memori.** Belum ada SQLDelight atau DataStore. Tawaran baru hilang begitu aplikasi ditutup. Perilakunya konsisten, tapi tetap menyesatkan kalau tidak diberi tahu saat demo.
3. **Lokasi masih hardcoded.** Koordinat pengguna di `PetaViewModel` masih ditulis langsung di kode. Belum ada permission lokasi maupun mock GPS. Jadi pengurutan berdasarkan jarak sudah benar secara logika, tapi masukannya belum asli.
4. **Peta masih placeholder.** Yang tampil masih kotak penanda, belum peta sungguhan. Komponen pemetaan belum dipilih karena akan menentukan dependency baru yang lumayan berat; kami tunda sampai Sprint 3.
5. **Belum ada UI test.** Syarat 3 UI test belum terpenuhi. Ini memang tertulis di rencana Sprint 4.
6. **iOS belum benar-benar dijalankan.** Konfigurasinya ada dan CI-nya hijau untuk tugas non-iOS, tapi kami belum pernah melihatnya jalan di simulator karena tidak punya Mac.
7. **Coverage belum diukur.** Kami belum memasang tool untuk mengukur persentase coverage, jadi klaim "di atas 50%" belum bisa dibuktikan. Ini akan ditambahkan di Sprint 4.

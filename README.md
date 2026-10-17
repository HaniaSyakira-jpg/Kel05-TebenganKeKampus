# Tebengan Ke Kampus 🚗🎓

[![CI](https://github.com/HaniaSyakira-jpg/Kel05-TebenganKeKampus/actions/workflows/ci.yml/badge.svg)](https://github.com/HaniaSyakira-jpg/Kel05-TebenganKeKampus/actions/workflows/ci.yml)

Aplikasi mobile multiplatform untuk **berbagi tumpangan (tebengan) menuju kampus ITERA**. Mahasiswa yang punya kendaraan dapat menawarkan sisa kursi, dan mahasiswa lain dapat mencari tumpangan terdekat dari titik jemputnya.

Dibangun dengan **Kotlin Multiplatform + Compose Multiplatform** sebagai Project Akhir mata kuliah **IF25-22017 Pengembangan Aplikasi Mobile**, Institut Teknologi Sumatera.

> **Fitur tema: Lokasi** — pencarian tumpangan diurutkan berdasarkan jarak titik jemput ke lokasi pengguna (rumus Haversine).

---

## 👥 Tim — Kelompok 05 PAM RA

| Nama | NIM | Role | GitHub |
|---|---|---|---|
| Refin Agus Saputra | 124140126 | Lead / Product & Integration | [@Avnzgs864](https://github.com/Avnzgs864) |
| Hania Syakira | 124140114 | Dev — UI/UX & Screens | [@HaniaSyakira-jpg](https://github.com/HaniaSyakira-jpg) |
| Yowen Daniel | 124140120 | Dev — Architecture, Data, CI/CD | [@wennn99](https://github.com/wennn99) |

> Dosen pengampu ditambahkan sebagai collaborator repositori untuk keperluan review.

---

## 📱 Deskripsi

**Tebengan Ke Kampus** menyelesaikan masalah nyata mahasiswa ITERA: transportasi menuju kampus yang mahal dan tidak efisien. Aplikasi mempertemukan pengemudi (penawar tebengan) dengan penumpang berdasarkan **kedekatan lokasi titik jemput**.

Alur utama:
1. Pengguna membuka **Beranda** dan melihat daftar tawaran tebengan.
2. Daftar diurutkan dari titik jemput yang **paling dekat** dengan lokasi pengguna.
3. Pengguna membuka **Detail** untuk melihat rute, jadwal, kuota kursi, dan ongkos.
4. Pengguna dapat **menambah** tawaran tebengan miliknya sendiri lewat form.
5. **Peta** menampilkan sebaran titik jemput; **Profil** dan **Pengaturan** melengkapi aplikasi.

---

## ✨ Fitur

### Fitur Minimum

- [x] **Beranda** — daftar tawaran tebengan (LazyColumn, Material 3)
- [x] **Detail** — detail tebengan dengan argument passing (`detail/{id}`)
- [x] **Tambah Tebengan** — form pembuatan tawaran (bagian dari CRUD)
- [x] **Peta** — sebaran titik jemput berbasis lokasi
- [x] **Profil** — informasi pengguna
- [x] **Pengaturan** — preferensi aplikasi (dark mode)
- [x] Navigasi multi-layar + bottom navigation
- [x] MVVM + Clean Architecture (presentation / domain / data)
- [x] State management dengan `StateFlow` + sealed UI state
- [x] Dependency Injection dengan **Koin**
- [x] Unit test (`kotlin.test` + `kotlinx-coroutines-test`)
- [ ] Local database dengan CRUD persisten (SQLDelight / DataStore) — *Sprint 3*
- [ ] REST API integration dengan Ktor Client — *Sprint 3*
- [ ] Minimal 10 unit test + 3 UI test, coverage > 50% — *Sprint 4*
- [ ] Animasi & polish UI/UX — *Sprint 4*

### Fitur Bonus

- [x] **CI/CD** — GitHub Actions build + test otomatis (+5%)
- [x] **Dark Mode** — toggle tema terang/gelap (+5%)
- [x] **iOS Support** — target `iosX64`, `iosArm64`, `iosSimulatorArm64` (+10%)
- [ ] **Offline First** — data tetap tersedia tanpa internet (+5%)
- [ ] **Play Store Ready** — signed APK/AAB (+5%)
- [ ] **AI Integration** — rekomendasi tebengan pintar (+10%, opsional)

---

## 🛠 Tech Stack

| Kategori | Teknologi |
|---|---|
| Framework | Kotlin Multiplatform 2.2.20, Compose Multiplatform 1.9.0 |
| Arsitektur | MVVM + Clean Architecture + Repository Pattern |
| Async & State | Coroutines 1.10.2, Flow, StateFlow |
| Networking | Ktor Client 3.2.3, Kotlinx Serialization 1.9.0 |
| DI | Koin 4.1.1 |
| Navigasi | JetBrains Navigation Compose 2.9.2 |
| Lifecycle | JetBrains Lifecycle 2.9.6 (ViewModel, `collectAsStateWithLifecycle`) |
| Testing | kotlin.test, kotlinx-coroutines-test |
| Build | Gradle 8.14.3 (Kotlin DSL + Version Catalog), AGP 8.11.2 |
| CI/CD | GitHub Actions (JDK 17, Android SDK) |
| Target | Android (minSdk 24, target/compileSdk 36), iOS 3 arch, Desktop JVM |

---

## 🏗 Arsitektur

Proyek mengikuti **Clean Architecture** dengan aturan dependensi mengarah ke dalam: `presentation → domain ← data`.

```
┌─────────────────────────────────────────────┐
│  PRESENTATION                               │
│  Screens (Compose) · ViewModels · UI State  │
│  navigation/ · theme/ · components/         │
└───────────────────┬─────────────────────────┘
                    │ depends on
┌───────────────────▼─────────────────────────┐
│  DOMAIN  (tanpa dependensi platform)        │
│  Use Cases · Domain Models · Repo Interface │
└───────────────────▲─────────────────────────┘
                    │ implements
┌───────────────────┴─────────────────────────┐
│  DATA                                       │
│  RepositoryImpl · Local/Remote DataSource   │
│  DTO & Mapper                               │
└─────────────────────────────────────────────┘
```

Detail lengkap ada di **[docs/ARCHITECTURE.md](docs/ARCHITECTURE.md)**.

### Struktur Proyek

```
composeApp/src/
├── commonMain/kotlin/id/ac/itera/tebengankekampus/
│   ├── App.kt                      # Root composable
│   ├── di/AppModule.kt             # Koin modules
│   ├── data/
│   │   ├── local/                  # TebenganLocalDataSource (seed data)
│   │   ├── remote/                 # HttpClientFactory (expect)
│   │   ├── repository/             # TebenganRepositoryImpl
│   │   └── model/                  # TebenganDto + mapper
│   ├── domain/
│   │   ├── model/                  # Tebengan, Pengguna, TitikLokasi (Haversine)
│   │   ├── repository/             # TebenganRepository (interface)
│   │   └── usecase/                # AmatiDaftar, Ambil, Tambah, Hapus, CariTerdekat
│   ├── presentation/
│   │   ├── navigation/             # Routes, AppNavHost
│   │   ├── theme/                  # Material 3 theme (light/dark)
│   │   ├── components/             # TebenganCard, StateComponents
│   │   └── screens/                # home, detail, tambah, peta, profil, pengaturan
│   └── util/Platform.kt            # expect
├── androidMain/                    # MainActivity, TebenganApplication, actual
├── desktopMain/                    # main.kt (preview cepat & test), actual
├── iosMain/                        # MainViewController, actual
└── commonTest/                     # Unit test
```

---

## 🚀 Setup

### Prasyarat
- Android Studio (Ladybug atau lebih baru) + JDK 17
- Android SDK Platform 36
- (Opsional) Xcode 15+ untuk menjalankan target iOS (hanya di macOS)

### Langkah
```bash
# 1. Clone repositori
git clone https://github.com/HaniaSyakira-jpg/Kel05-TebenganKeKampus.git
cd Kel05-TebenganKeKampus

# 2. Buka di Android Studio, lalu tunggu Gradle sync
#    (atau jalankan lewat terminal:)

# 3. Jalankan aplikasi Android (butuh emulator/device)
./gradlew :composeApp:installDebug

# 4. Menjalankan versi Desktop (tanpa emulator, untuk preview cepat)
./gradlew :composeApp:run

# 5. Menjalankan unit test
./gradlew :composeApp:desktopTest :composeApp:testDebugUnitTest

# 6. Build APK debug
./gradlew :composeApp:assembleDebug
```

Android Studio: pilih run configuration **composeApp** untuk Android, atau jalankan `:composeApp:run` untuk Desktop.

> `local.properties` (berisi path Android SDK) tidak di-commit. Android Studio membuatnya otomatis saat sync.

---

## 🔄 CI/CD

Workflow [`.github/workflows/ci.yml`](.github/workflows/ci.yml) berjalan pada setiap `push` dan `pull_request` ke `main`/`develop`:

1. Checkout + setup JDK 17 (Temurin, cache Gradle)
2. Setup Android SDK
3. `./gradlew :composeApp:testDebugUnitTest :composeApp:desktopTest` — menjalankan seluruh unit test
4. `./gradlew :composeApp:assembleDebug` — memastikan APK dapat dibangun
5. Upload laporan test sebagai artifact

Status badge di bagian atas README akan menampilkan **passing** bila seluruh langkah berhasil.

---

## 🌿 Branching & Konvensi Commit

Strategi branching: `main` ← `develop` ← `feature/*`

```bash
git checkout develop
git pull origin develop
git checkout -b feature/nama-fitur
# ...kerjakan fitur...
git add . && git commit -m "feat: implement nama fitur"
git push origin feature/nama-fitur
# Buka Pull Request di GitHub → minta review → merge
```

Konvensi commit: `feat:` · `fix:` · `refactor:` · `test:` · `docs:` · `style:` · `chore:`

---

## 📅 Rencana Sprint

| Sprint | Minggu | Fokus |
|---|---|---|
| **Sprint 1** | W11 | Planning & Setup — requirements, arsitektur, repo, CI/CD ✅ |
| Sprint 2 | W12 | Core Features — layar utama, navigasi, data layer, CRUD |
| Sprint 3 | W13 | Advanced Features — Ktor REST API, SQLDelight/offline-first, lokasi platform |
| Sprint 4 | W14 | Polish & Testing — UI/UX, bug fix, ≥10 unit test + 3 UI test |
| Sprint 5 | W15 | Final Preparation — perbaikan akhir, persiapan demo |
| UAS | W16 | Final Demo Day |

Rincian fitur per sprint dan **pembagian tugas** ada di **[docs/PROJECT_PLAN.md](docs/PROJECT_PLAN.md)**.

---

## 📚 Dokumentasi

| Dokumen | Isi |
|---|---|
| [README.md](README.md) | Ikhtisar, tim, fitur, setup, CI/CD |
| [docs/PROJECT_PLAN.md](docs/PROJECT_PLAN.md) | Requirements, fitur per sprint, task assignment, definisi selesai |
| [docs/ARCHITECTURE.md](docs/ARCHITECTURE.md) | Clean Architecture, layer, DI, alur data, keputusan teknis |

---

## 📄 Lisensi & Atribusi

Proyek akademik untuk mata kuliah IF25-22017 Pengembangan Aplikasi Mobile, Program Studi Teknik Informatika, Institut Teknologi Sumatera. Kode bersifat original kecuali disebutkan lain.

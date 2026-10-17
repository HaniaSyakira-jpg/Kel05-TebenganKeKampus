# Project Plan — Tebengan Ke Kampus

**Kelompok 05 PAM RA** · Mata kuliah IF25-22017 Pengembangan Aplikasi Mobile · ITERA
**Tema**: Tebengan Ke Kampus (berbagi tumpangan menuju kampus) · **Fitur tema**: Lokasi

Dokumen ini memuat *requirements*, *feature breakdown per sprint*, dan *task assignment* sesuai deliverable Sprint 1.

---

## 1. Requirements

### 1.1 Masalah & Solusi

| | |
|---|---|
| **Masalah** | Mahasiswa ITERA yang tinggal jauh dari kampus kesulitan dan mahal untuk sampai ke kampus; banyak kendaraan pribadi berangkat dengan kursi kosong. |
| **Solusi** | Aplikasi yang mempertemukan penawar tebengan (punya kendaraan) dengan pencari tebengan, diurutkan berdasarkan **kedekatan titik jemput**. |
| **Pengguna** | Mahasiswa ITERA (penumpang) dan mahasiswa ITERA yang membawa kendaraan (pengemudi/penawar). |

### 1.2 Functional Requirements

| ID | Requirement | Prioritas |
|---|---|---|
| FR-01 | Pengguna dapat melihat daftar tawaran tebengan | Wajib |
| FR-02 | Daftar tebengan terurut dari titik jemput terdekat ke lokasi pengguna | Wajib (tema) |
| FR-03 | Pengguna dapat membuka detail tebengan (rute, jadwal, kursi, ongkos) | Wajib |
| FR-04 | Pengguna dapat menambahkan tawaran tebengan baru | Wajib |
| FR-05 | Pengguna dapat menghapus tawaran tebengan miliknya | Wajib |
| FR-06 | Peta menampilkan sebaran titik jemput | Wajib |
| FR-07 | Pengguna dapat melihat dan mengubah profil | Wajib |
| FR-08 | Pengguna dapat mengubah preferensi aplikasi (dark mode) | Wajib |
| FR-09 | Data tebengan tersimpan lokal dan tersedia tanpa internet | Bonus (offline-first) |

### 1.3 Non-Functional Requirements

| ID | Requirement |
|---|---|
| NFR-01 | Minimal 5 layar, layout responsif, Material Design 3 |
| NFR-02 | MVVM + Clean Architecture (lapisan presentation / domain / data) |
| NFR-03 | State management dengan `StateFlow` dan sealed UI state |
| NFR-04 | Navigasi multi-layar dengan argument passing |
| NFR-05 | Dependency Injection dengan Koin |
| NFR-06 | Minimal 10 unit test, 3 UI test, coverage > 50% |
| NFR-07 | Berjalan di Android (wajib) dan iOS (bonus) dari satu codebase |
| NFR-08 | CI otomatis (build + test) hijau pada setiap push/PR |

---

## 2. Fitur per Sprint

### Sprint 1 — Planning & Setup (Minggu 11) ✅

Fokus: fondasi proyek, bukan fitur produk.

| Deliverable | Status |
|---|---|
| Repositori GitHub dengan seluruh anggota sebagai collaborator | ✅ |
| Struktur proyek KMP dengan folder arsitektur yang benar | ✅ |
| GitHub Actions CI (build + test) berjalan | ✅ |
| README lengkap (tim, deskripsi, fitur, tech stack, arsitektur) | ✅ |
| Dokumen project plan (fitur per sprint + task assignment) | ✅ |
| **Bonus**: setup Koin DI (+10%) | ✅ |

### Sprint 2 — Core Features (Minggu 12)

| Fitur | Terkait |
|---|---|
| Layar **Beranda** menampilkan daftar tebengan (LazyColumn) | FR-01 |
| Layar **Detail** + argument passing `detail/{id}` | FR-03 |
| Layar **Tambah Tebengan** (form + validasi) | FR-04 |
| Layar **Profil** dan bottom navigation | FR-07 |
| Data layer: repository, data source, model/mapper | NFR-02 |
| CRUD dasar (Create, Read, Delete) pada penyimpanan lokal | FR-04, FR-05 |
| Navigasi antar layar lengkap | NFR-04 |

### Sprint 3 — Advanced Features (Minggu 13)

| Fitur | Terkait |
|---|---|
| Integrasi **Ktor Client** + Kotlinx Serialization (REST API) | NFR-07 |
| Penyimpanan persisten **SQLDelight** + arsitektur offline-first | FR-09 |
| **Pencarian tebengan terdekat** berbasis lokasi nyata (permission + GPS) | FR-02 |
| Layar **Peta** menampilkan titik jemput penawaran | FR-06 |
| Filter/urutkan daftar berdasarkan jarak, waktu, ongkos | FR-02 |

### Sprint 4 — Polish & Testing (Minggu 14)

| Fitur | Terkait |
|---|---|
| Penyempurnaan UI/UX + animasi transisi | NFR-01 |
| **Dark mode** penuh (theme terang/gelap) | FR-08 |
| Minimal **10 unit test** + **3 UI test**, coverage > 50% | NFR-06 |
| Penanganan error & empty state yang rapi | NFR-01 |
| Perbaikan bug hasil review | — |

### Sprint 5 — Final Preparation (Minggu 15)

| Fitur | Terkait |
|---|---|
| Perbaikan akhir & stabilisasi | — |
| Signed APK/AAB (Play Store ready) | Bonus |
| Penyempurnaan dokumentasi & panduan demo | — |
| Latihan presentasi + demo | — |

### UAS — Final Demo Day (Minggu 16)

Demo aplikasi, live code review, dan tanya jawab.

---

## 3. Task Assignment

Pembagian peran mengikuti rekomendasi Sprint 1 (Lead, Dev, QA). Setiap anggota berkontribusi **commit** sebagai bukti kolaborasi (rubrik Team Collaboration 10%).

| Anggota | Role | Tanggung jawab utama |
|---|---|---|
| **Refin Agus Saputra** (124140126) | Lead / Product & Integration | Menjaga scope & requirements, integrasi antar fitur, koordinasi PR & review, penyusunan materi demo/presentasi |
| **Hania Syakira** (124140114) | Dev — UI/UX & Screens | Desain & implementasi layar Compose (Beranda, Detail, Tambah, Peta, Profil, Pengaturan), theme, komponen reusable, animasi |
| **Yowen Daniel** (124140120) | Dev — Architecture, Data & CI/CD | Struktur Clean Architecture, domain & data layer, use case, Koin DI, Ktor & penyimpanan lokal, unit test, pipeline CI/CD |

### 3.1 Pembagian Tugas per Sprint

| Sprint | Refin (Lead) | Hania (UI/UX) | Yowen (Arch/Data/CI) |
|---|---|---|---|
| **Sprint 1** | Requirements, pemilihan ide, dokumentasi plan, integrasi repo | Struktur presentasi layar, kontribusi README (bagian fitur) | Struktur proyek KMP, layer domain/data, DI Koin, CI/CD, unit test |
| **Sprint 2** | Review PR, verifikasi alur CRUD, integrasi navigasi | Beranda, Detail, Tambah, Profil, komponen & theme | Repository, data source, mapper, use case, ViewModel |
| **Sprint 3** | Review & integrasi API, QA manual | Layar Peta, UI filter/pencarian, empty/error state | Ktor Client, SQLDelight, permission lokasi, logic Haversine |
| **Sprint 4** | UAT, tracking bug, verifikasi coverage | Polish UI/UX, animasi, dark mode | Unit test & UI test, perbaikan bug data layer |
| **Sprint 5** | Materi demo, presentasi, sign-off | Final UI pass, screenshot & aset demo | Signed build, perbaikan akhir, dokumentasi teknis |

### 3.2 Definition of Done

Sebuah task dianggap selesai bila:
1. Kode dikompilasi tanpa error dan lolos `./gradlew :composeApp:testDebugUnitTest :composeApp:desktopTest`.
2. Ada unit test untuk logika non-UI (domain/data).
3. Sudah melalui Pull Request dan di-review minimal satu anggota lain.
4. CI GitHub Actions berstatus **passing**.
5. Dokumentasi (README/komentar) diperbarui bila mengubah perilaku.

---

## 4. Alur Kerja Git

```
main        ← kode production-ready, dilindungi, hanya lewat PR
  ↑ merge
develop     ← integrasi, tujuan default PR harian
  ↑ merge
feature/*   ← feature/beranda-screen, feature/ktor-api, dst.
```

- Commit mengikuti konvensi: `feat:` · `fix:` · `refactor:` · `test:` · `docs:` · `style:` · `chore:`
- Satu Pull Request = satu fitur/perbaikan yang fokus.
- Template PR memuat deskripsi, daftar perubahan, checklist (compile, test, no warnings).

---

## 5. Risiko & Mitigasi

| Risiko | Mitigasi |
|---|---|
| Scope creep (terlalu banyak bonus) | Kerjakan fitur wajib dulu; bonus hanya bila waktu tersisa |
| Target iOS butuh macOS/Xcode | Siapkan target iOS lebih awal; uji di mesin macOS bila tersedia |
| Data lokasi palsu saat dev | Gunakan titik lokasi seed (koordinat ITERA) sampai GPS asli terpasang |
| Kontribusi tidak merata | Task assignment jelas + review PR wajib + cek riwayat commit |
| CI merah memblokir PR | Jalankan test lokal sebelum push; perbaiki CI segera |

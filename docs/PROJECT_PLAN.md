# Rencana Proyek — Tebengan Ke Kampus

Kelompok 05 PAM RA · IF25-22017 Pengembangan Aplikasi Mobile · ITERA

Dokumen ini isinya kebutuhan aplikasi, rencana tiap sprint, dan pembagian tugas anggota. Ini yang diminta sebagai salah satu deliverable Sprint 1.

## 1. Latar belakang dan batasan

Masalah yang mau diselesaikan: mahasiswa yang tinggal jauh dari kampus sering kesulitan dan mahal untuk sampai ke kampus, padahal banyak juga yang berangkat bawa kendaraan dengan kursi kosong. Aplikasi ini mencoba mempertemukan dua kelompok itu.

Penggunanya dua macam, dan kebutuhannya beda:

- **Pencari tebengan** — mau lihat daftar tawaran yang relevan, terutama yang titik jemputnya dekat. Dia butuh daftar yang sudah terurut, bukan daftar acak.
- **Penawar tebengan** — mau memasang tawaran dengan cepat. Kalau form-nya ribet, dia tidak akan pakai.

Batasan yang kami sadari sejak awal:

- Waktu pengerjaan cuma 5 sprint (Sprint 1-5), sambil ada praktikum lain. Jadi fitur bonus hanya dikerjakan kalau fitur wajib sudah beres.
- Tim 3 orang, jadi tidak realistis mengerjakan Ktor, SQLDelight, GPS asli, dan AI sekaligus di sprint yang sama.
- Tidak ada backend sendiri. Kalau nanti butuh API, kami akan pakai layanan publik atau mock server; keputusan ini belum final.

## 2. Kebutuhan fungsional

| Kode | Kebutuhan | Prioritas |
|---|---|---|
| FR-01 | Lihat daftar tawaran tebengan | Wajib |
| FR-02 | Daftar terurut dari titik jemput terdekat ke lokasi pengguna | Wajib (tema) |
| FR-03 | Buka detail tebengan: rute, jadwal, kursi, ongkos, kontak | Wajib |
| FR-04 | Tambah tawaran tebengan baru | Wajib |
| FR-05 | Hapus tawaran milik sendiri | Wajib |
| FR-06 | Peta sebaran titik jemput | Wajib |
| FR-07 | Lihat dan ubah profil | Wajib |
| FR-08 | Ubah preferensi aplikasi (mode gelap) | Wajib |
| FR-09 | Data tetap ada tanpa internet | Bonus |

FR-02 adalah inti tema kelompok kami. Implementasinya sekarang ada di `CariTebenganTerdekatUseCase`, yang mengurutkan daftar berdasarkan hasil `TitikLokasi.jarakKmKe()` (rumus Haversine). Logika ini ditaruh di layer domain supaya bisa diuji murni tanpa perlu GPS atau perangkat.

## 3. Kebutuhan non-fungsional

- Minimal 5 layar, layout responsif, Material Design 3
- MVVM dengan pemisahan layer presentation / domain / data
- State pakai `StateFlow` + sealed UI state
- Navigasi multi-layar dengan argument passing
- Dependency injection pakai Koin
- Minimal 10 unit test dan 3 UI test, coverage di atas 50%
- Jalan di Android (wajib) dan iOS (bonus)
- CI otomatis: build dan test hijau tiap push

## 4. Rencana per sprint

### Sprint 1 — Planning dan setup — selesai

Sprint ini tidak menghasilkan fitur produk, isinya fondasi:

- Repo GitHub dengan anggota sebagai collaborator
- Struktur proyek KMP dengan folder per layer
- CI GitHub Actions (build + test)
- README dan dokumen rencana proyek
- Bonus: setup Koin DI

### Sprint 2 — Core features

- Beranda menampilkan daftar tebengan
- Detail tebengan + passing data lewat route
- Form tambah tawaran
- Profil dan bottom navigation
- Repository, data source, model + mapper
- CRUD dasar (create, read, delete)

### Sprint 3 — Advanced features

- Integrasi Ktor + Kotlinx Serialization untuk REST API
- Ganti penyimpanan in-memory jadi SQLDelight
- Ambil lokasi asli dari GPS, termasuk permission-nya
- Peta yang benar-benar menampilkan titik jemput
- Filter dan urutan berdasarkan jarak, waktu, atau ongkos

### Sprint 4 — Polish dan testing

- Rapikan UI, tambah animasi transisi
- Mode gelap lengkap
- Lengkapi sampai minimal 10 unit test + 3 UI test
- Empty state dan error state yang layak
- Perbaikan bug hasil review

Sprint 3-4 ini yang paling padat. Kami sadar risikonya paling besar di situ karena Ktor dan SQLDelight belum pernah kami pakai di proyek sebelumnya; kalau waktunya mepet, yang kami tunda dulu adalah filter dan animasi, bukan penyimpanan data.

### Sprint 5 — Persiapan akhir

- Perbaikan akhir dan stabilisasi
- Signed APK/AAB
- Rapikan dokumentasi dan siapkan bahan demo

## 5. Pembagian tugas

Peran mengikuti saran di materi: lead, developer, QA. Semua anggota tetap menulis kode, bukan hanya satu orang.

| Anggota | Peran | Tanggung jawab |
|---|---|---|
| Refin Agus Saputra | Ketua | Menjaga scope dan kebutuhan, integrasi antar fitur, review pull request, bahan presentasi |
| Hania Syakira | Developer UI/UX | Layar Compose, komponen yang dipakai berulang, theme, animasi |
| Yowen Daniel | Developer arsitektur/data | Struktur layer, domain dan data, Koin, Ktor dan penyimpanan lokal, unit test, CI/CD |

Pembagian per sprint:

| Sprint | Refin | Hania | Yowen |
|---|---|---|---|
| 1 | Kebutuhan, pilih ide, dokumen rencana | Rancangan tampilan, bagian fitur di README | Struktur proyek, layer domain/data, Koin, CI, unit test |
| 2 | Review PR, cek alur CRUD | Beranda, Detail, Tambah, Profil, komponen | Repository, data source, mapper, use case, ViewModel |
| 3 | Review integrasi API, QA manual | Layar Peta, filter, empty/error state | Ktor, SQLDelight, permission lokasi, Haversine |
| 4 | UAT, catat bug, cek coverage | Polish UI, animasi, mode gelap | Unit test, UI test, perbaikan bug data layer |
| 5 | Materi demo dan presentasi | Final pass UI, aset demo | Signed build, dokumentasi teknis |

## 6. Definisi selesai

Sebuah task dianggap selesai kalau:

1. Kodenya bisa dikompilasi dan `./gradlew :composeApp:testDebugUnitTest :composeApp:desktopTest` lulus
2. Ada unit test untuk logika non-UI
3. Sudah lewat pull request dan di-review minimal satu anggota lain
4. CI hijau
5. Dokumentasi diperbarui kalau perilakunya berubah

## 7. Alur kerja git

```
main        kode siap pakai, tidak boleh push langsung
  ↑
develop     integrasi harian
  ↑
feature/*   satu branch per fitur
```

Commit pakai format `feat:`, `fix:`, `refactor:`, `test:`, `docs:`, `style:`, `chore:`. Satu pull request untuk satu fitur supaya review-nya tidak melebar.

## 8. Risiko dan catatan

| Risiko | Cara menanganinya |
|---|---|
| Fitur bonus kebanyakan | Kerjakan yang wajib dulu, bonus hanya kalau ada sisa waktu |
| Ktor dan SQLDelight belum pernah dipakai | Latihan di luar jam praktikum sebelum Sprint 3 mulai |
| Target iOS perlu macOS | Siapkan targetnya lebih awal, uji jalan kalau ada akses Mac |
| Kontribusi tidak merata | Tugas per sprint jelas + review PR wajib |
| CI merah menghambat PR | Jalankan test lokal dulu sebelum push |

Catatan yang sudah kami temukan sendiri saat Sprint 1:

- Form Tambah Tebengan punya nilai awal (default) di beberapa field tapi tidak di "Titik jemput", jadi tampilannya terlihat tidak konsisten. Sudah dicatat untuk dirapikan di Sprint 2.
- Beberapa dependency Ktor sudah masuk build config tapi belum dipakai. Ini disengaja, tapi tetap utang teknis yang perlu dibereskan di Sprint 3.
- Data masih in-memory, jadi tawaran baru hilang kalau aplikasi ditutup. Perlu diperjelas di demo supaya tidak disangka bug.

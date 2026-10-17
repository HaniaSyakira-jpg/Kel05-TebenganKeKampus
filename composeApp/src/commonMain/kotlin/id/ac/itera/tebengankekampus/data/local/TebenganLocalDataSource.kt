package id.ac.itera.tebengankekampus.data.local

import id.ac.itera.tebengankekampus.domain.model.Tebengan
import id.ac.itera.tebengankekampus.domain.model.TitikLokasi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

/**
 * Sumber data lokal sementara (in-memory) untuk Sprint 1.
 * Pada Sprint 3 sumber data ini akan diperkaya dengan SQLDelight + DataStore
 * agar aplikasi bisa bekerja offline (offline-first).
 */
class TebenganLocalDataSource {
    private val daftar = MutableStateFlow(SampleData.tebengan)

    val tebengan: Flow<List<Tebengan>> = daftar

    fun semua(): List<Tebengan> = daftar.value

    fun cari(id: String): Tebengan? = daftar.value.firstOrNull { it.id == id }

    fun simpan(tebengan: Tebengan) {
        daftar.update { list -> list.filterNot { it.id == tebengan.id } + tebengan }
    }

    fun hapus(id: String) {
        daftar.update { list -> list.filterNot { it.id == id } }
    }
}

/** Data contoh agar aplikasi langsung bisa didemokan sebelum ada backend. */
object SampleData {
    val tebengan: List<Tebengan> = listOf(
        Tebengan(
            id = "TBN-001",
            namaPengemudi = "Refin Agus Saputra",
            kontakPengemudi = "0812-0000-0001",
            asal = TitikLokasi("Gerbang ITERA", -5.3588, 105.3148),
            tujuan = TitikLokasi("Kampus ITERA - Gedung A", -5.3595, 105.3118),
            jadwal = "2026-02-10T07:00:00",
            kursiTersedia = 3,
            hargaPerOrang = 5000,
            catatan = "Kumpul di gerbang utama, toleransi 5 menit.",
        ),
        Tebengan(
            id = "TBN-002",
            namaPengemudi = "Hania Syakira",
            kontakPengemudi = "0812-0000-0002",
            asal = TitikLokasi("Asrama ITERA", -5.3570, 105.3160),
            tujuan = TitikLokasi("Kampus ITERA - Gedung B", -5.3601, 105.3122),
            jadwal = "2026-02-10T07:15:00",
            kursiTersedia = 2,
            hargaPerOrang = 4000,
            catatan = "Lewat jalan belakang asrama.",
        ),
        Tebengan(
            id = "TBN-003",
            namaPengemudi = "Yowen Daniel",
            kontakPengemudi = "0812-0000-0003",
            asal = TitikLokasi("Pasar Sukarame", -5.3702, 105.2973),
            tujuan = TitikLokasi("Kampus ITERA - Gerbang Utama", -5.3588, 105.3148),
            jadwal = "2026-02-10T06:45:00",
            kursiTersedia = 4,
            hargaPerOrang = 6000,
            catatan = "Berangkat tepat waktu.",
        ),
        Tebengan(
            id = "TBN-004",
            namaPengemudi = "Refin Agus Saputra",
            kontakPengemudi = "0812-0000-0001",
            asal = TitikLokasi("Terminal Rajabasa", -5.3661, 105.2327),
            tujuan = TitikLokasi("Kampus ITERA - Gedung C", -5.3610, 105.3130),
            jadwal = "2026-02-11T06:30:00",
            kursiTersedia = 2,
            hargaPerOrang = 10000,
            catatan = "Mohon konfirmasi lewat WhatsApp sebelum pukul 05.30.",
        ),
    )
}

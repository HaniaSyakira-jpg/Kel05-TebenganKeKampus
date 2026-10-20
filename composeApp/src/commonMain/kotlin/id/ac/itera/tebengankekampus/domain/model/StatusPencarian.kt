package id.ac.itera.tebengankekampus.domain.model

/**
 * Status proses pencarian tebengan berbasis lokasi.
 *
 * Kelas tersegel supaya konsumen (ViewModel, demo konsol, maupun test) bisa
 * melakukan `when` yang lengkap tanpa cabang `else`. Status berubah seiring
 * waktu dan dikirim lewat [kotlinx.coroutines.flow.Flow] oleh
 * [id.ac.itera.tebengankekampus.data.local.FakeTebenganDataSource].
 */
sealed class StatusPencarian {
    /** Pencarian belum/sedang diminta. */
    data object Menunggu : StatusPencarian()

    /** Data sedang diambil dari sumber data. */
    data object Memuat : StatusPencarian()

    /** Pencarian selesai dan hasilnya tersedia, sudah terurut dari terdekat. */
    data class Sukses(val hasil: List<TebenganTerdekat>) : StatusPencarian()

    /** Pencarian gagal, misalnya tidak ada tebengan dalam radius. */
    data class Gagal(val pesan: String) : StatusPencarian()
}

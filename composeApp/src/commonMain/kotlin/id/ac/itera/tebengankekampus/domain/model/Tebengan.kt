package id.ac.itera.tebengankekampus.domain.model

/** Status sebuah tawaran tebengan. */
enum class StatusTebengan {
    TERBUKA,
    PENUH,
    SELESAI,
}

/**
 * Model domain utama aplikasi: satu tawaran tumpangan (tebengan) ke kampus.
 */
data class Tebengan(
    val id: String,
    val namaPengemudi: String,
    val kontakPengemudi: String,
    val asal: TitikLokasi,
    val tujuan: TitikLokasi,
    val jadwal: String,
    val kursiTersedia: Int,
    val hargaPerOrang: Int,
    val catatan: String = "",
    val status: StatusTebengan = StatusTebengan.TERBUKA,
)

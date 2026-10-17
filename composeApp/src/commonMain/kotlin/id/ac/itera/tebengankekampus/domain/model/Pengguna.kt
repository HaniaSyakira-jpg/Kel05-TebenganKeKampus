package id.ac.itera.tebengankekampus.domain.model

/** Model domain untuk pengguna aplikasi (mahasiswa). */
data class Pengguna(
    val id: String,
    val nama: String,
    val nim: String,
    val programStudi: String,
    val noTelepon: String,
)

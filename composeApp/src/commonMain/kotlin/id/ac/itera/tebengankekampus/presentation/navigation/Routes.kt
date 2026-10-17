package id.ac.itera.tebengankekampus.presentation.navigation

/** Kumpulan rute navigasi aplikasi. */
object Routes {
    const val BERANDA = "beranda"
    const val PETA = "peta"
    const val PROFIL = "profil"
    const val PENGATURAN = "pengaturan"
    const val TAMBAH = "tambah"
    const val DETAIL = "detail/{id}"

    fun detail(id: String): String = "detail/$id"
}

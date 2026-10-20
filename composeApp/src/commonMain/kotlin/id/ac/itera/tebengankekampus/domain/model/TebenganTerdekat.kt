package id.ac.itera.tebengankekampus.domain.model

/**
 * Hasil pencarian: satu tebengan beserta jarak titik jemputnya
 * ke titik pengguna (km).
 */
data class TebenganTerdekat(
    val tebengan: Tebengan,
    val jarakKm: Double,
)

package id.ac.itera.tebengankekampus.domain.model

/**
 * Permintaan pencarian tebengan berbasis lokasi (tema kelompok: Lokasi).
 * Menyimpan titik jemput pengguna dan batas radius maksimum pencarian (km).
 */
data class PencarianLokasi(
    val titikSaya: TitikLokasi,
    val radiusMaksKm: Double,
)

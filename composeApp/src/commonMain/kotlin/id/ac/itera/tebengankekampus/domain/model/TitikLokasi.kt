package id.ac.itera.tebengankekampus.domain.model

import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * Titik lokasi sederhana (tanpa dependensi platform) yang dipakai sebagai
 * asal/tujuan sebuah tebengan. Fitur tema aplikasi adalah "Lokasi".
 */
data class TitikLokasi(
    val nama: String,
    val latitude: Double,
    val longitude: Double,
) {
    /** Jarak garis lurus ke titik lain (km) memakai rumus Haversine. */
    fun jarakKmKe(lain: TitikLokasi): Double {
        val radiusBumiKm = 6371.0
        val dLat = PI / 180 * (lain.latitude - latitude)
        val dLon = PI / 180 * (lain.longitude - longitude)
        val a = sin(dLat / 2) * sin(dLat / 2) +
            cos(PI / 180 * latitude) * cos(PI / 180 * lain.latitude) *
            sin(dLon / 2) * sin(dLon / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return radiusBumiKm * c
    }
}

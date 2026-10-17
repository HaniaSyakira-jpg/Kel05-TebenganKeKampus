package id.ac.itera.tebengankekampus.domain.model

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TitikLokasiTest {

    @Test
    fun jarakKeTitikYangSamaAdalahNol() {
        val titik = TitikLokasi("ITERA", -5.3588, 105.3148)
        assertEquals(0.0, titik.jarakKmKe(titik), 0.0001)
    }

    @Test
    fun jarakDihitungDenganBenar() {
        val gerbang = TitikLokasi("Gerbang ITERA", -5.3588, 105.3148)
        val gedungA = TitikLokasi("Gedung A", -5.3595, 105.3118)
        val jarak = gerbang.jarakKmKe(gedungA)
        assertTrue(jarak > 0.0, "Jarak harus lebih besar dari nol")
        assertTrue(jarak < 1.0, "Jarak dua titik di kampus seharusnya kurang dari 1 km")
    }
}

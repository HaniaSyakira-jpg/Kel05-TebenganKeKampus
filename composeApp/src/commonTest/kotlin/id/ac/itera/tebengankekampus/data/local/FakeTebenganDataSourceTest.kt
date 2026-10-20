package id.ac.itera.tebengankekampus.data.local

import id.ac.itera.tebengankekampus.domain.model.PencarianLokasi
import id.ac.itera.tebengankekampus.domain.model.StatusPencarian
import id.ac.itera.tebengankekampus.domain.model.StatusTebengan
import id.ac.itera.tebengankekampus.domain.model.TitikLokasi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Bukti berjalan Checkpoint Minggu 2.
 * Test ini sekaligus mencetak perubahan status ke konsol supaya terlihat
 * saat `./gradlew :composeApp:desktopTest` dijalankan.
 */
class FakeTebenganDataSourceTest {

    private val sumber = FakeTebenganDataSource()

    @Test
    fun alurPencarianBerubahDariMenungguKeSukses() = runTest {
        val pencarian = PencarianLokasi(
            titikSaya = TitikLokasi("Asrama ITERA", -5.3570, 105.3160),
            radiusMaksKm = 15.0,
        )

        val status = sumber.amatiPencarian(pencarian).toList()

        println("== Alur status pencarian ==")
        status.forEachIndexed { indeks, s -> println("[$indeks] $s") }

        assertTrue(status.first() is StatusPencarian.Menunggu)
        assertTrue(status[1] is StatusPencarian.Memuat)
        assertTrue(status.last() is StatusPencarian.Sukses)
    }

    @Test
    fun hasilPencarianTerurutDariYangTerdekat() = runTest {
        val pencarian = PencarianLokasi(
            titikSaya = TitikLokasi("Gerbang ITERA", -5.3588, 105.3148),
            radiusMaksKm = 15.0,
        )

        val sukses = sumber.amatiPencarian(pencarian).toList()
            .filterIsInstance<StatusPencarian.Sukses>()
            .last()

        val jarak = sukses.hasil.map { it.jarakKm }
        assertEquals(jarak.sorted(), jarak)
        assertTrue(sukses.hasil.isNotEmpty())
    }

    @Test
    fun perubahanStatusTercetakKeKonsol() = runTest {
        val daftar = sumber.amatiPerubahanStatus("TBN-001").toList()

        println("== Flow perubahan status TBN-001 ==")
        daftar.forEachIndexed { indeks, t ->
            println("[$indeks] ${t.id} ${t.namaPengemudi} -> ${t.status}")
        }

        assertEquals(
            listOf(StatusTebengan.TERBUKA, StatusTebengan.PENUH, StatusTebengan.SELESAI),
            daftar.map { it.status },
        )
    }

    @Test
    fun pencarianDiLuarRadiusMenghasilkanGagal() = runTest {
        val pencarian = PencarianLokasi(
            titikSaya = TitikLokasi("Lokasi Jauh", -5.5000, 105.4000),
            radiusMaksKm = 0.1,
        )

        val status = sumber.amatiPencarian(pencarian).toList()

        assertTrue(status.last() is StatusPencarian.Gagal)
    }
}

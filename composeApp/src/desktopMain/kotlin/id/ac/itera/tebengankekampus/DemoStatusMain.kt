package id.ac.itera.tebengankekampus

import id.ac.itera.tebengankekampus.data.local.FakeTebenganDataSource
import id.ac.itera.tebengankekampus.domain.model.PencarianLokasi
import id.ac.itera.tebengankekampus.domain.model.StatusPencarian
import id.ac.itera.tebengankekampus.domain.model.TitikLokasi
import kotlinx.coroutines.runBlocking

/**
 * Bukti berjalan Checkpoint Minggu 2.
 *
 * Dijalankan lewat tugas Gradle `:composeApp:runDemoStatus`, atau langsung dari
 * Android Studio dengan menjalankan fungsi `main` ini. Tidak membuka jendela
 * supaya keluaran konsol mudah dibaca.
 */
fun main() {
    val sumber = FakeTebenganDataSource()

    println("=== Demo 1: alur status pencarian (delay + Flow) ===")
    runBlocking {
        val pencarian = PencarianLokasi(
            titikSaya = TitikLokasi("Asrama ITERA", -5.3570, 105.3160),
            radiusMaksKm = 15.0,
        )
        sumber.amatiPencarian(pencarian).collect { status ->
            when (status) {
                StatusPencarian.Menunggu -> println("[status] Menunggu...")
                StatusPencarian.Memuat -> println("[status] Memuat data...")
                is StatusPencarian.Sukses -> {
                    println("[status] Sukses, ${status.hasil.size} tebengan terdekat:")
                    status.hasil.forEachIndexed { indeks, hasil ->
                        val jarak = "%.2f".format(hasil.jarakKm)
                        println(
                            "   ${indeks + 1}. ${hasil.tebengan.id} - ${hasil.tebengan.namaPengemudi} " +
                                "(${hasil.tebengan.asal.nama}) - $jarak km",
                        )
                    }
                }

                is StatusPencarian.Gagal -> println("[status] Gagal: ${status.pesan}")
            }
        }
    }

    println()
    println("=== Demo 2: Flow perubahan status tebengan ===")
    runBlocking {
        sumber.amatiPerubahanStatus("TBN-001").collect { tebengan ->
            println("[status] ${tebengan.id} ${tebengan.namaPengemudi} -> ${tebengan.status}")
        }
    }
}

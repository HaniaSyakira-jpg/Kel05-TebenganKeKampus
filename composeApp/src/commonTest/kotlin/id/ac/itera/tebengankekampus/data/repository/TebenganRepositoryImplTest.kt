package id.ac.itera.tebengankekampus.data.repository

import id.ac.itera.tebengankekampus.data.local.TebenganLocalDataSource
import id.ac.itera.tebengankekampus.domain.model.Tebengan
import id.ac.itera.tebengankekampus.domain.model.TitikLokasi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class TebenganRepositoryImplTest {

    private fun buatTebengan(id: String, pengemudi: String = "Tester") = Tebengan(
        id = id,
        namaPengemudi = pengemudi,
        kontakPengemudi = "0800",
        asal = TitikLokasi("A", -5.35, 105.31),
        tujuan = TitikLokasi("B", -5.36, 105.32),
        jadwal = "2026-01-01T07:00:00",
        kursiTersedia = 2,
        hargaPerOrang = 5000,
    )

    @Test
    fun daftarAwalBerisiDataContoh() = runTest {
        val repository = TebenganRepositoryImpl(TebenganLocalDataSource())
        assertTrue(repository.amatiDaftarTebengan().first().isNotEmpty())
    }

    @Test
    fun tambahTebenganMenyimpanData() = runTest {
        val repository = TebenganRepositoryImpl(TebenganLocalDataSource())
        repository.tambahTebengan(buatTebengan("TBN-BARU", "Yowen Daniel"))

        val hasil = repository.ambilTebengan("TBN-BARU")
        assertEquals("Yowen Daniel", hasil?.namaPengemudi)
    }

    @Test
    fun hapusTebenganMenghilangkanData() = runTest {
        val repository = TebenganRepositoryImpl(TebenganLocalDataSource())
        repository.tambahTebengan(buatTebengan("TBN-HAPUS"))
        repository.hapusTebengan("TBN-HAPUS")

        assertNull(repository.ambilTebengan("TBN-HAPUS"))
    }

    @Test
    fun ambilTebenganTidakAdaMengembalikanNull() = runTest {
        val repository = TebenganRepositoryImpl(TebenganLocalDataSource())
        assertNull(repository.ambilTebengan("TIDAK-ADA"))
    }
}

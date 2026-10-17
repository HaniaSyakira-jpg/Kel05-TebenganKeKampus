package id.ac.itera.tebengankekampus.domain.usecase

import id.ac.itera.tebengankekampus.data.local.TebenganLocalDataSource
import id.ac.itera.tebengankekampus.data.repository.TebenganRepositoryImpl
import id.ac.itera.tebengankekampus.domain.model.TitikLokasi
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CariTebenganTerdekatUseCaseTest {

    @Test
    fun hasilDiurutkanDariYangTerdekat() = runTest {
        val repository = TebenganRepositoryImpl(TebenganLocalDataSource())
        val useCase = CariTebenganTerdekatUseCase(repository)

        // Titik ini persis di "Gerbang ITERA", sehingga harus muncul paling awal.
        val titikSaya = TitikLokasi("Gerbang ITERA", -5.3588, 105.3148)
        val hasil = useCase(titikSaya)

        assertTrue(hasil.isNotEmpty())
        assertEquals("Gerbang ITERA", hasil.first().asal.nama)

        // Memastikan benar-benar terurut menaik berdasarkan jarak.
        val jarakList = hasil.map { it.asal.jarakKmKe(titikSaya) }
        assertEquals(jarakList.sorted(), jarakList)
    }
}

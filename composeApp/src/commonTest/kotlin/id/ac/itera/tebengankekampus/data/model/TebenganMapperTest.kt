package id.ac.itera.tebengankekampus.data.model

import id.ac.itera.tebengankekampus.domain.model.StatusTebengan
import kotlin.test.Test
import kotlin.test.assertEquals

class TebenganMapperTest {

    @Test
    fun dtoKeDomainMemetakanSemuaField() {
        val dto = TebenganDto(
            id = "TBN-999",
            namaPengemudi = "Tester",
            kontakPengemudi = "0800",
            asal = TitikLokasiDto("A", -5.1, 105.1),
            tujuan = TitikLokasiDto("B", -5.2, 105.2),
            jadwal = "2026-01-01T07:00:00",
            kursiTersedia = 2,
            hargaPerOrang = 5000,
            catatan = "catatan",
            status = "PENUH",
        )

        val domain = dto.toDomain()

        assertEquals("TBN-999", domain.id)
        assertEquals("A", domain.asal.nama)
        assertEquals(StatusTebengan.PENUH, domain.status)
        assertEquals(2, domain.kursiTersedia)
    }

    @Test
    fun statusTidakDikenalFallbackKeTerbuka() {
        val dto = TebenganDto(
            id = "TBN-1",
            namaPengemudi = "A",
            kontakPengemudi = "B",
            asal = TitikLokasiDto("A", 0.0, 0.0),
            tujuan = TitikLokasiDto("B", 0.0, 0.0),
            jadwal = "x",
            kursiTersedia = 1,
            hargaPerOrang = 0,
            status = "STATUS_ANEHD",
        )
        assertEquals(StatusTebengan.TERBUKA, dto.toDomain().status)
    }

    @Test
    fun bolakBalikDomainDtoTetapSama() {
        val dto = TebenganDto(
            id = "TBN-123",
            namaPengemudi = "Tester",
            kontakPengemudi = "0800",
            asal = TitikLokasiDto("A", -5.1, 105.1),
            tujuan = TitikLokasiDto("B", -5.2, 105.2),
            jadwal = "2026-01-01T07:00:00",
            kursiTersedia = 3,
            hargaPerOrang = 4000,
            status = "SELESAI",
        )
        assertEquals(dto, dto.toDomain().toDto())
    }
}

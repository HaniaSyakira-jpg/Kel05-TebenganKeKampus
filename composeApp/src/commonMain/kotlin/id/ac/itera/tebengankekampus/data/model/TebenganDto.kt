package id.ac.itera.tebengankekampus.data.model

import id.ac.itera.tebengankekampus.domain.model.StatusTebengan
import id.ac.itera.tebengankekampus.domain.model.Tebengan
import id.ac.itera.tebengankekampus.domain.model.TitikLokasi
import kotlinx.serialization.Serializable

/** DTO untuk pertukaran data dengan REST API (dipakai mulai Sprint 3). */
@Serializable
data class TitikLokasiDto(
    val nama: String,
    val latitude: Double,
    val longitude: Double,
)

@Serializable
data class TebenganDto(
    val id: String,
    val namaPengemudi: String,
    val kontakPengemudi: String,
    val asal: TitikLokasiDto,
    val tujuan: TitikLokasiDto,
    val jadwal: String,
    val kursiTersedia: Int,
    val hargaPerOrang: Int,
    val catatan: String = "",
    val status: String = "TERBUKA",
)

fun TitikLokasiDto.toDomain(): TitikLokasi = TitikLokasi(nama, latitude, longitude)

fun TebenganDto.toDomain(): Tebengan = Tebengan(
    id = id,
    namaPengemudi = namaPengemudi,
    kontakPengemudi = kontakPengemudi,
    asal = asal.toDomain(),
    tujuan = tujuan.toDomain(),
    jadwal = jadwal,
    kursiTersedia = kursiTersedia,
    hargaPerOrang = hargaPerOrang,
    catatan = catatan,
    status = runCatching { StatusTebengan.valueOf(status) }.getOrDefault(StatusTebengan.TERBUKA),
)

fun TitikLokasi.toDto(): TitikLokasiDto = TitikLokasiDto(nama, latitude, longitude)

fun Tebengan.toDto(): TebenganDto = TebenganDto(
    id = id,
    namaPengemudi = namaPengemudi,
    kontakPengemudi = kontakPengemudi,
    asal = asal.toDto(),
    tujuan = tujuan.toDto(),
    jadwal = jadwal,
    kursiTersedia = kursiTersedia,
    hargaPerOrang = hargaPerOrang,
    catatan = catatan,
    status = status.name,
)

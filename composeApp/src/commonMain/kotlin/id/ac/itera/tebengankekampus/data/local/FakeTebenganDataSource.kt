package id.ac.itera.tebengankekampus.data.local

import id.ac.itera.tebengankekampus.domain.model.PencarianLokasi
import id.ac.itera.tebengankekampus.domain.model.StatusPencarian
import id.ac.itera.tebengankekampus.domain.model.StatusTebengan
import id.ac.itera.tebengankekampus.domain.model.Tebengan
import id.ac.itera.tebengankekampus.domain.model.TebenganTerdekat
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Sumber data palsu (fake) untuk Checkpoint Minggu 2.
 *
 * Tidak memanggil jaringan dan tidak menyimpan apa pun secara permanen. Ia dipakai
 * untuk menunjukkan alur status yang sama seperti nantinya saat REST API masuk:
 * data diambil dengan jeda (`delay`) dan hasilnya dikirim lewat [Flow].
 *
 * @param dataAwal daftar tebengan awal, bisa diganti saat pengujian.
 * @param jedaMs lama jeda simulasi jaringan (ms). Di `runTest` nilai ini dilewati
 *   otomatis oleh waktu virtual, jadi pengujian tidak perlu menunggu.
 */
class FakeTebenganDataSource(
    private val dataAwal: List<Tebengan> = SampleData.tebengan,
    private val jedaMs: Long = 600L,
) {

    /**
     * Mencari tebengan terdekat dan mengembalikan alurnya sebagai [Flow].
     *
     * Urutan status: [StatusPencarian.Menunggu] -> [StatusPencarian.Memuat] ->
     * [StatusPencarian.Sukses] (atau [StatusPencarian.Gagal] bila radius kosong).
     */
    fun amatiPencarian(pencarian: PencarianLokasi): Flow<StatusPencarian> = flow {
        emit(StatusPencarian.Menunggu)
        delay(jedaMs)
        emit(StatusPencarian.Memuat)
        delay(jedaMs)

        val hasil = dataAwal
            .map { TebenganTerdekat(it, it.asal.jarakKmKe(pencarian.titikSaya)) }
            .filter { it.jarakKm <= pencarian.radiusMaksKm }
            .sortedBy { it.jarakKm }

        if (hasil.isEmpty()) {
            emit(StatusPencarian.Gagal("Tidak ada tebengan dalam radius ${pencarian.radiusMaksKm} km."))
        } else {
            emit(StatusPencarian.Sukses(hasil))
        }
    }

    /**
     * Mengamati perubahan status satu tebengan dari waktu ke waktu.
     *
     * Alurnya: `TERBUKA` -> `PENUH` -> `SELESAI`, masing-masing dipisah jeda.
     */
    fun amatiPerubahanStatus(id: String): Flow<Tebengan> = flow {
        val awal = dataAwal.firstOrNull { it.id == id } ?: return@flow

        emit(awal)
        delay(jedaMs)
        emit(awal.copy(status = StatusTebengan.PENUH))
        delay(jedaMs)
        emit(awal.copy(status = StatusTebengan.SELESAI))
    }
}

package id.ac.itera.tebengankekampus.domain.usecase

import id.ac.itera.tebengankekampus.domain.model.Tebengan
import id.ac.itera.tebengankekampus.domain.model.TitikLokasi
import id.ac.itera.tebengankekampus.domain.repository.TebenganRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

/** Mengamati daftar tebengan secara realtime. */
class AmatiDaftarTebenganUseCase(private val repository: TebenganRepository) {
    operator fun invoke(): Flow<List<Tebengan>> = repository.amatiDaftarTebengan()
}

/** Mengambil satu tebengan berdasarkan id. */
class AmbilTebenganUseCase(private val repository: TebenganRepository) {
    suspend operator fun invoke(id: String): Tebengan? = repository.ambilTebengan(id)
}

/** Menambahkan tawaran tebengan baru. */
class TambahTebenganUseCase(private val repository: TebenganRepository) {
    suspend operator fun invoke(tebengan: Tebengan): Tebengan = repository.tambahTebengan(tebengan)
}

/** Menghapus tawaran tebengan. */
class HapusTebenganUseCase(private val repository: TebenganRepository) {
    suspend operator fun invoke(id: String) = repository.hapusTebengan(id)
}

/**
 * Mencari tebengan dan mengurutkannya berdasarkan jarak titik jemput
 * ke lokasi pengguna. Fitur unggulan sesuai tema "Lokasi".
 */
class CariTebenganTerdekatUseCase(private val repository: TebenganRepository) {
    suspend operator fun invoke(titikSaya: TitikLokasi): List<Tebengan> =
        repository.amatiDaftarTebengan().first()
            .sortedBy { it.asal.jarakKmKe(titikSaya) }
}

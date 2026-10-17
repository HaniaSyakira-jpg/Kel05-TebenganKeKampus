package id.ac.itera.tebengankekampus.data.repository

import id.ac.itera.tebengankekampus.data.local.TebenganLocalDataSource
import id.ac.itera.tebengankekampus.domain.model.Tebengan
import id.ac.itera.tebengankekampus.domain.repository.TebenganRepository
import kotlinx.coroutines.flow.Flow

/**
 * Implementasi [TebenganRepository].
 * Saat ini memakai sumber data lokal. Integrasi REST API (Ktor) dan
 * penyimpanan permanen akan ditambahkan pada Sprint 3.
 */
class TebenganRepositoryImpl(
    private val localDataSource: TebenganLocalDataSource,
) : TebenganRepository {

    override fun amatiDaftarTebengan(): Flow<List<Tebengan>> = localDataSource.tebengan

    override suspend fun ambilTebengan(id: String): Tebengan? = localDataSource.cari(id)

    override suspend fun tambahTebengan(tebengan: Tebengan): Tebengan {
        localDataSource.simpan(tebengan)
        return tebengan
    }

    override suspend fun hapusTebengan(id: String) {
        localDataSource.hapus(id)
    }
}

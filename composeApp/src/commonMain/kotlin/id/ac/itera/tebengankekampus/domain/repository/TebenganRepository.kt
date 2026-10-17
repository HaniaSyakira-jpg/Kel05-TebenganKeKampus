package id.ac.itera.tebengankekampus.domain.repository

import id.ac.itera.tebengankekampus.domain.model.Tebengan
import kotlinx.coroutines.flow.Flow

/**
 * Kontrak repositori pada lapisan domain.
 * Implementasi konkret berada di lapisan data (Clean Architecture).
 */
interface TebenganRepository {
    fun amatiDaftarTebengan(): Flow<List<Tebengan>>
    suspend fun ambilTebengan(id: String): Tebengan?
    suspend fun tambahTebengan(tebengan: Tebengan): Tebengan
    suspend fun hapusTebengan(id: String)
}

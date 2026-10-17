package id.ac.itera.tebengankekampus.presentation.screens.peta

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.ac.itera.tebengankekampus.domain.model.Tebengan
import id.ac.itera.tebengankekampus.domain.model.TitikLokasi
import id.ac.itera.tebengankekampus.domain.usecase.CariTebenganTerdekatUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn

data class HasilTerdekat(
    val tebengan: Tebengan,
    val jarakKm: Double,
)

sealed interface PetaUiState {
    data object Memuat : PetaUiState
    data class Sukses(
        val lokasiSaya: TitikLokasi,
        val hasil: List<HasilTerdekat>,
    ) : PetaUiState
}

/**
 * Menampilkan daftar tebengan yang diurutkan berdasarkan jarak titik jemput
 * ke lokasi pengguna. Lokasi masih di-mock; integrasi GPS platform
 * (expect/actual + permission) direncanakan pada Sprint 3.
 */
class PetaViewModel(
    private val cariTebenganTerdekatUseCase: CariTebenganTerdekatUseCase,
) : ViewModel() {

    private val lokasiSaya = TitikLokasi(
        nama = "Lokasi Saya (Asrama ITERA)",
        latitude = -5.3570,
        longitude = 105.3160,
    )

    val uiState: StateFlow<PetaUiState> = flow {
        emit(PetaUiState.Memuat)
        val hasil = cariTebenganTerdekatUseCase(lokasiSaya)
            .map { HasilTerdekat(tebengan = it, jarakKm = it.asal.jarakKmKe(lokasiSaya)) }
        emit(PetaUiState.Sukses(lokasiSaya, hasil))
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = PetaUiState.Memuat,
    )
}

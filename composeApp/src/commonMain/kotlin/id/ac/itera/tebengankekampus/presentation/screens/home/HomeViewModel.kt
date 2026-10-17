package id.ac.itera.tebengankekampus.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.ac.itera.tebengankekampus.domain.model.Tebengan
import id.ac.itera.tebengankekampus.domain.usecase.AmatiDaftarTebenganUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

sealed interface HomeUiState {
    data object Memuat : HomeUiState
    data class Sukses(val daftar: List<Tebengan>) : HomeUiState
    data object Kosong : HomeUiState
    data class Galat(val pesan: String) : HomeUiState
}

class HomeViewModel(
    amatiDaftarTebenganUseCase: AmatiDaftarTebenganUseCase,
) : ViewModel() {

    val uiState: StateFlow<HomeUiState> = amatiDaftarTebenganUseCase()
        .map { daftar -> if (daftar.isEmpty()) HomeUiState.Kosong else HomeUiState.Sukses(daftar) }
        .catch { emit(HomeUiState.Galat(it.message ?: "Gagal memuat data")) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = HomeUiState.Memuat,
        )
}

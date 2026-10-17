package id.ac.itera.tebengankekampus.presentation.screens.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.ac.itera.tebengankekampus.domain.model.Tebengan
import id.ac.itera.tebengankekampus.domain.usecase.AmbilTebenganUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed interface DetailUiState {
    data object Memuat : DetailUiState
    data object TidakDitemukan : DetailUiState
    data class Sukses(val tebengan: Tebengan) : DetailUiState
}

class DetailViewModel(
    private val ambilTebenganUseCase: AmbilTebenganUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<DetailUiState>(DetailUiState.Memuat)
    val uiState: StateFlow<DetailUiState> = _uiState

    fun muat(id: String) {
        viewModelScope.launch {
            val tebengan = ambilTebenganUseCase(id)
            _uiState.value = if (tebengan == null) {
                DetailUiState.TidakDitemukan
            } else {
                DetailUiState.Sukses(tebengan)
            }
        }
    }
}

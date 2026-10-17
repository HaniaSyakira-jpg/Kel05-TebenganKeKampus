package id.ac.itera.tebengankekampus.presentation.screens.profil

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.ac.itera.tebengankekampus.domain.model.Pengguna
import id.ac.itera.tebengankekampus.domain.usecase.AmatiDaftarTebenganUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class ProfilUiState(
    val pengguna: Pengguna = Pengguna(
        id = "124140120",
        nama = "Yowen Daniel",
        nim = "124140120",
        programStudi = "Teknik Informatika",
        noTelepon = "0812-0000-0003",
    ),
    val jumlahTebenganSaya: Int = 0,
    val jumlahTebenganTersedia: Int = 0,
)

class ProfilViewModel(
    amatiDaftarTebenganUseCase: AmatiDaftarTebenganUseCase,
) : ViewModel() {

    private val pengguna = ProfilUiState().pengguna

    val uiState: StateFlow<ProfilUiState> = amatiDaftarTebenganUseCase()
        .map { daftar ->
            ProfilUiState(
                pengguna = pengguna,
                jumlahTebenganSaya = daftar.count { it.namaPengemudi == pengguna.nama },
                jumlahTebenganTersedia = daftar.size,
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ProfilUiState(),
        )
}

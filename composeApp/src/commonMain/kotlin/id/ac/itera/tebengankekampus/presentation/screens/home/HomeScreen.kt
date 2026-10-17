package id.ac.itera.tebengankekampus.presentation.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import id.ac.itera.tebengankekampus.presentation.components.EmptyState
import id.ac.itera.tebengankekampus.presentation.components.ErrorState
import id.ac.itera.tebengankekampus.presentation.components.LoadingState
import id.ac.itera.tebengankekampus.presentation.components.TebenganCard
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onLihatDetail: (String) -> Unit,
    onTawarkan: () -> Unit,
    viewModel: HomeViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Tebengan Ke Kampus") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = onTawarkan) {
                Text("＋")
            }
        },
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            when (val state = uiState) {
                HomeUiState.Memuat -> LoadingState()
                HomeUiState.Kosong -> EmptyState("Belum ada tawaran tebengan.")
                is HomeUiState.Galat -> ErrorState(state.pesan)
                is HomeUiState.Sukses -> LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    item {
                        Text(
                            text = "Halo! Berikut tebengan yang tersedia hari ini 🚗",
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                    items(state.daftar, key = { it.id }) { tebengan ->
                        TebenganCard(
                            tebengan = tebengan,
                            onClick = { onLihatDetail(tebengan.id) },
                        )
                    }
                }
            }
        }
    }
}

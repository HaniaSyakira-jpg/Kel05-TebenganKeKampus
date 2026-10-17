package id.ac.itera.tebengankekampus.presentation.screens.peta

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import id.ac.itera.tebengankekampus.presentation.components.LoadingState
import id.ac.itera.tebengankekampus.util.platformName
import org.koin.compose.viewmodel.koinViewModel
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetaScreen(
    viewModel: PetaViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Tebengan Terdekat") }) },
    ) { padding ->
        when (val state = uiState) {
            PetaUiState.Memuat -> LoadingState(pesan = "Mencari tebengan terdekat...")
            is PetaUiState.Sukses -> LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                item {
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp)
                                .background(MaterialTheme.colorScheme.surfaceVariant),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = "🗺️\nTitik tengah: ${state.lokasiSaya.nama}",
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.bodyMedium,
                            )
                        }
                    }
                }
                item {
                    Text(
                        text = "Diurutkan dari yang paling dekat (${platformName()}):",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
                items(state.hasil, key = { it.tebengan.id }) { hasil ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "${hasil.tebengan.asal.nama}  →  ${hasil.tebengan.tujuan.nama}",
                                style = MaterialTheme.typography.titleMedium,
                            )
                            Text(
                                text = "Jarak dari lokasimu: ± ${hasil.jarakKm.roundToInt()} km",
                                style = MaterialTheme.typography.bodyMedium,
                            )
                            Text(
                                text = "Pengemudi: ${hasil.tebengan.namaPengemudi} • Rp${hasil.tebengan.hargaPerOrang}",
                                style = MaterialTheme.typography.bodySmall,
                            )
                        }
                    }
                }
            }
        }
    }
}

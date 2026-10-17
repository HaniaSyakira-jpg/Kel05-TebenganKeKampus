package id.ac.itera.tebengankekampus.presentation.screens.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import id.ac.itera.tebengankekampus.presentation.components.ErrorState
import id.ac.itera.tebengankekampus.presentation.components.LoadingState
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    id: String,
    onKembali: () -> Unit,
    viewModel: DetailViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(id) { viewModel.muat(id) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detail Tebengan") },
                navigationIcon = {
                    TextButton(onClick = onKembali) { Text("← Kembali") }
                },
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            when (val state = uiState) {
                DetailUiState.Memuat -> LoadingState()
                DetailUiState.TidakDitemukan -> ErrorState("Tebengan tidak ditemukan.")
                is DetailUiState.Sukses -> {
                    val tebengan = state.tebengan
                    Text(
                        text = "${tebengan.asal.nama}  →  ${tebengan.tujuan.nama}",
                        style = MaterialTheme.typography.headlineSmall,
                    )
                    HorizontalDivider()
                    BarisDetail("Pengemudi", tebengan.namaPengemudi)
                    BarisDetail("Kontak", tebengan.kontakPengemudi)
                    BarisDetail("Jadwal", tebengan.jadwal)
                    BarisDetail("Kursi tersedia", tebengan.kursiTersedia.toString())
                    BarisDetail("Harga", "Rp${tebengan.hargaPerOrang} / orang")
                    BarisDetail("Status", tebengan.status.name)
                    BarisDetail("Titik jemput", "${tebengan.asal.nama} (${tebengan.asal.latitude}, ${tebengan.asal.longitude})")
                    BarisDetail("Tujuan", "${tebengan.tujuan.nama} (${tebengan.tujuan.latitude}, ${tebengan.tujuan.longitude})")
                    BarisDetail("Catatan", tebengan.catatan.ifBlank { "-" })
                }
            }
        }
    }
}

@Composable
private fun BarisDetail(label: String, nilai: String) {
    Column {
        Text(text = label, style = MaterialTheme.typography.labelMedium)
        Text(text = nilai, style = MaterialTheme.typography.bodyLarge)
    }
}

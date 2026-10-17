package id.ac.itera.tebengankekampus.presentation.screens.profil

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfilScreen(
    onPengaturan: () -> Unit,
    viewModel: ProfilViewModel = koinViewModel(),
) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Profil Saya") }) },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "👤 ${state.pengguna.nama}", style = MaterialTheme.typography.titleLarge)
                    Text(text = "NIM: ${state.pengguna.nim}", style = MaterialTheme.typography.bodyMedium)
                    Text(text = state.pengguna.programStudi, style = MaterialTheme.typography.bodyMedium)
                    Text(text = "Kontak: ${state.pengguna.noTelepon}", style = MaterialTheme.typography.bodyMedium)
                }
            }

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Statistik", style = MaterialTheme.typography.titleMedium)
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    Text(text = "Tebengan saya: ${state.jumlahTebenganSaya}", style = MaterialTheme.typography.bodyLarge)
                    Text(text = "Tebengan tersedia: ${state.jumlahTebenganTersedia}", style = MaterialTheme.typography.bodyLarge)
                }
            }

            Button(onClick = onPengaturan, modifier = Modifier.fillMaxWidth()) {
                Text("Pengaturan")
            }
        }
    }
}

package id.ac.itera.tebengankekampus.presentation.screens.pengaturan

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import id.ac.itera.tebengankekampus.util.platformName

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PengaturanScreen(
    darkMode: Boolean,
    onToggleDarkMode: () -> Unit,
    onKembali: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pengaturan") },
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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "Mode Gelap", style = MaterialTheme.typography.titleMedium)
                        Text(text = "Ikuti tema gelap aplikasi", style = MaterialTheme.typography.bodySmall)
                    }
                    Switch(checked = darkMode, onCheckedChange = { onToggleDarkMode() })
                }
            }

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Tentang Aplikasi", style = MaterialTheme.typography.titleMedium)
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    Text(text = "Tebengan Ke Kampus", style = MaterialTheme.typography.bodyLarge)
                    Text(text = "Versi 1.0.0 • Sprint 1", style = MaterialTheme.typography.bodyMedium)
                    Text(text = "Platform: ${platformName()}", style = MaterialTheme.typography.bodyMedium)
                    Text(
                        text = "Kelompok 05 - PAM RA, Teknik Informatika ITERA",
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
            }
        }
    }
}

package id.ac.itera.tebengankekampus.presentation.screens.tambah

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TambahTebenganScreen(
    onSelesai: () -> Unit,
    viewModel: TambahTebenganViewModel = koinViewModel(),
) {
    val form by viewModel.form.collectAsState()

    LaunchedEffect(form.tersimpan) {
        if (form.tersimpan) onSelesai()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tawarkan Tebengan") },
                navigationIcon = {
                    TextButton(onClick = onSelesai) { Text("← Batal") }
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
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text("Isi detail perjalananmu", style = MaterialTheme.typography.titleMedium)

            OutlinedTextField(
                value = form.asal,
                onValueChange = viewModel::ubahAsal,
                label = { Text("Titik jemput") },
                placeholder = { Text("contoh: Gerbang ITERA") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )
            OutlinedTextField(
                value = form.tujuan,
                onValueChange = viewModel::ubahTujuan,
                label = { Text("Tujuan") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )
            OutlinedTextField(
                value = form.jadwal,
                onValueChange = viewModel::ubahJadwal,
                label = { Text("Jadwal (ISO-8601)") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )
            OutlinedTextField(
                value = form.kursi,
                onValueChange = viewModel::ubahKursi,
                label = { Text("Jumlah kursi") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )
            OutlinedTextField(
                value = form.harga,
                onValueChange = viewModel::ubahHarga,
                label = { Text("Harga per orang (Rp)") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )
            OutlinedTextField(
                value = form.catatan,
                onValueChange = viewModel::ubahCatatan,
                label = { Text("Catatan (opsional)") },
                modifier = Modifier.fillMaxWidth(),
            )

            form.pesanGalat?.let { pesan ->
                Text(
                    text = pesan,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }

            Button(
                onClick = viewModel::simpan,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Simpan Tebengan")
            }
        }
    }
}

package id.ac.itera.tebengankekampus

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import id.ac.itera.tebengankekampus.presentation.navigation.AppNavHost
import id.ac.itera.tebengankekampus.presentation.theme.TebenganTheme

/** Titik masuk UI yang dipakai oleh seluruh platform (Android, iOS, Desktop). */
@Composable
fun App() {
    var darkMode by remember { mutableStateOf(false) }

    TebenganTheme(darkTheme = darkMode) {
        AppNavHost(
            darkMode = darkMode,
            onToggleDarkMode = { darkMode = !darkMode },
        )
    }
}

package id.ac.itera.tebengankekampus.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val HijauItera = Color(0xFF1B5E20)
private val HijauTerang = Color(0xFF4C8C4A)
private val KuningAksen = Color(0xFFFFB300)
private val MerahItera = Color(0xFFB71C1C)

private val SkemaTerang = lightColorScheme(
    primary = HijauItera,
    onPrimary = Color.White,
    secondary = HijauTerang,
    tertiary = KuningAksen,
    error = MerahItera,
)

private val SkemaGelap = darkColorScheme(
    primary = HijauTerang,
    onPrimary = Color.White,
    secondary = HijauItera,
    tertiary = KuningAksen,
    error = MerahItera,
)

@Composable
fun TebenganTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (darkTheme) SkemaGelap else SkemaTerang,
        content = content,
    )
}

package id.ac.itera.tebengankekampus

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import id.ac.itera.tebengankekampus.di.appModule
import org.koin.core.context.startKoin

fun main() {
    startKoin {
        modules(appModule)
    }
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "Tebengan Ke Kampus",
        ) {
            App()
        }
    }
}

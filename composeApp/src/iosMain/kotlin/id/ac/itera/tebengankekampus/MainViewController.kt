package id.ac.itera.tebengankekampus

import androidx.compose.ui.window.ComposeUIViewController
import id.ac.itera.tebengankekampus.di.appModule
import org.koin.core.context.startKoin
import platform.UIKit.UIViewController

/** Entry point iOS: dipanggil dari SwiftUI (iosApp). */
fun MainViewController(): UIViewController {
    startKoin {
        modules(appModule)
    }
    return ComposeUIViewController { App() }
}

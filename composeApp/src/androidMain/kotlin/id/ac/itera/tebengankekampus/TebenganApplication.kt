package id.ac.itera.tebengankekampus

import android.app.Application
import id.ac.itera.tebengankekampus.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class TebenganApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@TebenganApplication)
            modules(appModule)
        }
    }
}

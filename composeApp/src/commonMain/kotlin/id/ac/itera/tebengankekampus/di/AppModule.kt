package id.ac.itera.tebengankekampus.di

import id.ac.itera.tebengankekampus.data.local.TebenganLocalDataSource
import id.ac.itera.tebengankekampus.data.repository.TebenganRepositoryImpl
import id.ac.itera.tebengankekampus.domain.repository.TebenganRepository
import id.ac.itera.tebengankekampus.domain.usecase.AmbilTebenganUseCase
import id.ac.itera.tebengankekampus.domain.usecase.AmatiDaftarTebenganUseCase
import id.ac.itera.tebengankekampus.domain.usecase.CariTebenganTerdekatUseCase
import id.ac.itera.tebengankekampus.domain.usecase.HapusTebenganUseCase
import id.ac.itera.tebengankekampus.domain.usecase.TambahTebenganUseCase
import id.ac.itera.tebengankekampus.presentation.screens.detail.DetailViewModel
import id.ac.itera.tebengankekampus.presentation.screens.home.HomeViewModel
import id.ac.itera.tebengankekampus.presentation.screens.peta.PetaViewModel
import id.ac.itera.tebengankekampus.presentation.screens.profil.ProfilViewModel
import id.ac.itera.tebengankekampus.presentation.screens.tambah.TambahTebenganViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

/**
 * Definisi dependency injection dengan Koin (bonus Sprint 1).
 * Struktur: data source -> repository -> use case -> view model.
 */
val appModule = module {
    // Data layer
    single { TebenganLocalDataSource() }
    single<TebenganRepository> { TebenganRepositoryImpl(get()) }

    // Domain layer (use cases)
    factory { AmatiDaftarTebenganUseCase(get()) }
    factory { AmbilTebenganUseCase(get()) }
    factory { TambahTebenganUseCase(get()) }
    factory { HapusTebenganUseCase(get()) }
    factory { CariTebenganTerdekatUseCase(get()) }

    // Presentation layer (view models)
    viewModelOf(::HomeViewModel)
    viewModelOf(::DetailViewModel)
    viewModelOf(::TambahTebenganViewModel)
    viewModelOf(::PetaViewModel)
    viewModelOf(::ProfilViewModel)
}

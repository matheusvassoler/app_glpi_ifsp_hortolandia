package com.glpi.ifsp.hortolandia.infrastructure.di

import com.glpi.ifsp.hortolandia.ui.viewmodel.LoginViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { LoginViewModel(get()) }
}

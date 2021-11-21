package com.glpi.ifsp.hortolandia.infrastructure.di

import com.glpi.ifsp.hortolandia.ui.viewmodel.LoginViewModel
import com.glpi.ifsp.hortolandia.ui.viewmodel.OpenTicketViewModel
import com.glpi.ifsp.hortolandia.ui.viewmodel.ProfileViewModel
import com.glpi.ifsp.hortolandia.ui.viewmodel.TicketViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { LoginViewModel(get()) }
    viewModel { ProfileViewModel(get()) }
    viewModel { TicketViewModel(get()) }
    viewModel { OpenTicketViewModel(get()) }
}

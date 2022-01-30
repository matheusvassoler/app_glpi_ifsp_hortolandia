package com.glpi.ifsp.hortolandia.infrastructure.di

import com.glpi.ifsp.hortolandia.domain.CreateTicketUseCase
import com.glpi.ifsp.hortolandia.domain.GetFormUseCase
import com.glpi.ifsp.hortolandia.domain.GetItemUseCase
import com.glpi.ifsp.hortolandia.domain.GetLocationUseCase
import com.glpi.ifsp.hortolandia.domain.GetTicketsUseCase
import com.glpi.ifsp.hortolandia.domain.LoginUseCase
import com.glpi.ifsp.hortolandia.domain.LogoutUseCase
import com.glpi.ifsp.hortolandia.domain.SessionUseCase
import org.koin.dsl.module

val domainModule = module {
    single { LoginUseCase(get(), get()) }
    single { SessionUseCase(get()) }
    single { LogoutUseCase(get(), get()) }
    single { GetTicketsUseCase(get(), get(), get()) }
    single { GetItemUseCase(get(), get()) }
    single { GetLocationUseCase(get(), get()) }
    single { GetFormUseCase(get(), get(), get(), get()) }
    single { CreateTicketUseCase(get(), get()) }
}

package com.glpi.ifsp.hortolandia.infrastructure.di

import com.glpi.ifsp.hortolandia.domain.LoginUseCase
import com.glpi.ifsp.hortolandia.domain.LogoutUseCase
import com.glpi.ifsp.hortolandia.domain.SessionUseCase
import org.koin.dsl.module

val domainModule = module {
    single { LoginUseCase(get(), get()) }
    single { SessionUseCase(get()) }
    single { LogoutUseCase(get(), get()) }
}

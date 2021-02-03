package com.glpi.ifsp.hortolandia.infrastructure.di

import com.glpi.ifsp.hortolandia.domain.SessionUseCase
import org.koin.dsl.module

val domainModule = module {
    single { SessionUseCase(get()) }
}

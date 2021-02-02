package com.glpi.ifsp.hortolandia.infrastructure.di

import com.glpi.ifsp.hortolandia.data.repository.session.SessionRemoteRepository
import com.glpi.ifsp.hortolandia.data.repository.session.SessionRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<SessionRepository> { SessionRemoteRepository(get()) }
}

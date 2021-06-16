package com.glpi.ifsp.hortolandia.infrastructure.di

import com.glpi.ifsp.hortolandia.data.repository.login.LoginRemoteRepository
import com.glpi.ifsp.hortolandia.data.repository.login.LoginRepository
import com.glpi.ifsp.hortolandia.data.repository.session.SessionLocalRepository
import com.glpi.ifsp.hortolandia.data.repository.session.SessionRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<LoginRepository> { LoginRemoteRepository(get()) }
    single<SessionRepository> { SessionLocalRepository(get()) }
}

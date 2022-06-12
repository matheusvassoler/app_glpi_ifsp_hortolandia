package com.glpi.ifsp.hortolandia.infrastructure.di

import com.glpi.ifsp.hortolandia.data.repository.form.FormRemoteRepository
import com.glpi.ifsp.hortolandia.data.repository.form.FormRepository
import com.glpi.ifsp.hortolandia.data.repository.item.ItemRemoteRepository
import com.glpi.ifsp.hortolandia.data.repository.item.ItemRepository
import com.glpi.ifsp.hortolandia.data.repository.location.LocationRemoteRepository
import com.glpi.ifsp.hortolandia.data.repository.location.LocationRepository
import com.glpi.ifsp.hortolandia.data.repository.login.LoginRemoteRepository
import com.glpi.ifsp.hortolandia.data.repository.login.LoginRepository
import com.glpi.ifsp.hortolandia.data.repository.logout.LogoutRemoteRepository
import com.glpi.ifsp.hortolandia.data.repository.logout.LogoutRepository
import com.glpi.ifsp.hortolandia.data.repository.profile.ProfileRemoteRepository
import com.glpi.ifsp.hortolandia.data.repository.profile.ProfileRepository
import com.glpi.ifsp.hortolandia.data.repository.session.SessionLocalRepository
import com.glpi.ifsp.hortolandia.data.repository.session.SessionRepository
import com.glpi.ifsp.hortolandia.data.repository.ticket.TicketRemoteRepository
import com.glpi.ifsp.hortolandia.data.repository.ticket.TicketRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<LoginRepository> { LoginRemoteRepository(get()) }
    single<SessionRepository> { SessionLocalRepository(get()) }
    single<LogoutRepository> { LogoutRemoteRepository(get()) }
    single<TicketRepository> { TicketRemoteRepository(get()) }
    single<FormRepository> { FormRemoteRepository(get()) }
    single<ItemRepository> { ItemRemoteRepository(get()) }
    single<LocationRepository> { LocationRemoteRepository(get()) }
    single<ProfileRepository> { ProfileRemoteRepository(get()) }
}

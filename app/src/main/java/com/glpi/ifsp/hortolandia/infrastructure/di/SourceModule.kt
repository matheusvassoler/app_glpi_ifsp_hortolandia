package com.glpi.ifsp.hortolandia.infrastructure.di

import com.glpi.ifsp.hortolandia.data.source.local.AppSharedPreferences
import com.glpi.ifsp.hortolandia.data.source.local.AppSharedPreferencesImpl
import com.glpi.ifsp.hortolandia.data.source.remote.ApiClient
import org.koin.dsl.module

val sourceModule = module {
    single { ApiClient() }
    single<AppSharedPreferences> { AppSharedPreferencesImpl(get()) }
}

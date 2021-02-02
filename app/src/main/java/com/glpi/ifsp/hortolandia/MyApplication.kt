package com.glpi.ifsp.hortolandia

import android.app.Application
import com.glpi.ifsp.hortolandia.infrastructure.di.repositoryModule
import com.glpi.ifsp.hortolandia.infrastructure.di.sourceModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MyApplication)
            modules(
                listOf(
                    sourceModule,
                    repositoryModule
                )
            )
        }
    }
}

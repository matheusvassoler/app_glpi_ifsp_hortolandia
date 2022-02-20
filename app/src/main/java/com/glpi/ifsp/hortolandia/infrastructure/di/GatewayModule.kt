package com.glpi.ifsp.hortolandia.infrastructure.di

import com.glpi.ifsp.hortolandia.gateway.DefaultLabelGateway
import com.glpi.ifsp.hortolandia.gateway.LabelGateway
import org.koin.dsl.module

val gatewayModule = module {
    single<LabelGateway> { DefaultLabelGateway(get()) }
}

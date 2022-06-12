package com.glpi.ifsp.hortolandia.ui.event

sealed class SplashScreenEvent {
    object OpenHome : SplashScreenEvent()
    object OpenLogin : SplashScreenEvent()
}

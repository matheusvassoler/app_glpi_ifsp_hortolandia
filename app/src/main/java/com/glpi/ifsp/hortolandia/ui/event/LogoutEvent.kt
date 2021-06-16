package com.glpi.ifsp.hortolandia.ui.event

sealed class LogoutEvent {
    object GoToLogin : LogoutEvent()
    object ShowLogoutError : LogoutEvent()
    object ShowInternalError : LogoutEvent()
}

package com.glpi.ifsp.hortolandia.ui.state

sealed class LogoutState {
    object ShowLoading : LogoutState()
    object HideLoading : LogoutState()
}

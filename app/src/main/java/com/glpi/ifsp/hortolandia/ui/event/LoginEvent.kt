package com.glpi.ifsp.hortolandia.ui.event

sealed class LoginEvent {
    object OpenHome : LoginEvent()
    object ShowBadCredentialError : LoginEvent()
    object ShowConnectionError : LoginEvent()
}

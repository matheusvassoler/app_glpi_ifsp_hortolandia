package com.glpi.ifsp.hortolandia.ui.state

sealed class LoginState {
    object ShowLoading : LoginState()
    object HideLoading : LoginState()
    data class ValidateField(val isUsernameEmpty: Boolean, val isPasswordEmpty: Boolean) : LoginState()
}

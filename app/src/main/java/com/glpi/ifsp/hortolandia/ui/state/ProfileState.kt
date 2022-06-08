package com.glpi.ifsp.hortolandia.ui.state

sealed class ProfileState {
    object ShowLoading : ProfileState()
    object HideLoading : ProfileState()
    data class ShowPersonalData(
        val id: Int,
        val username: String,
        val firstName: String,
        val lastName: String
    ) : ProfileState()
}

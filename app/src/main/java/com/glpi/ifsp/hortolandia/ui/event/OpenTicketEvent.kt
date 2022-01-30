package com.glpi.ifsp.hortolandia.ui.event

sealed class OpenTicketEvent {
    object OpenRegistrationScreenSuccessfully : OpenTicketEvent()
    data class OpenFailedRegistrationScreen(
        val ticketTitle: String,
        val answersToSave: HashMap<String, String>
    ) : OpenTicketEvent()
    object OpenLoginScreen : OpenTicketEvent()
}

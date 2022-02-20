package com.glpi.ifsp.hortolandia.ui.event

sealed class TicketEvent {
    object ShowInternalError : TicketEvent()
    object OpenLoginScreen : TicketEvent()
}

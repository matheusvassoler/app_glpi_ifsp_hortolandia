package com.glpi.ifsp.hortolandia.ui.state

import com.glpi.ifsp.hortolandia.ui.model.FormUI

sealed class OpenTicketState {
    data class ShowFormUI(val formUI: FormUI) : OpenTicketState()
    object ShowLoading : OpenTicketState()
    object ShowResponseRequestError : OpenTicketState()
    object ShowNullResponseBodyError : OpenTicketState()
}

package com.glpi.ifsp.hortolandia.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.glpi.ifsp.hortolandia.domain.CreateTicketUseCase
import com.glpi.ifsp.hortolandia.domain.GetFormUseCase
import com.glpi.ifsp.hortolandia.infrastructure.exceptions.ResponseRequestException
import com.glpi.ifsp.hortolandia.infrastructure.exceptions.UnauthorizedLoginException
import com.glpi.ifsp.hortolandia.ui.event.OpenTicketEvent
import com.glpi.ifsp.hortolandia.ui.state.OpenTicketState
import com.hadilq.liveevent.LiveEvent
import kotlinx.coroutines.launch

class OpenTicketViewModel(
    private val getFormUseCase: GetFormUseCase,
    private val createTicketUseCase: CreateTicketUseCase
) : ViewModel() {

    private var _state = MutableLiveData<OpenTicketState>()
    val state: LiveData<OpenTicketState>
        get() = _state

    private var _event = LiveEvent<OpenTicketEvent>()
    val event: LiveData<OpenTicketEvent>
        get() = _event

    fun onStart(formId: Int?) {
        viewModelScope.launch {
            try {
                _state.value = OpenTicketState.ShowLoading
                if (formId == null) {
                    throw ResponseRequestException()
                }
                val formUI = getFormUseCase(formId)
                _state.value = OpenTicketState.ShowFormUI(formUI)
            } catch (e: UnauthorizedLoginException) {
                _event.value = OpenTicketEvent.OpenLoginScreen
            } catch (e: Exception) {
                _state.value = OpenTicketState.ShowResponseRequestError
            }
        }
    }

    fun createTicket(ticketTitle: String, answersToSave: HashMap<String, String>) {
        viewModelScope.launch {
            try {
                _state.value = OpenTicketState.ShowLoading
                createTicketUseCase(ticketTitle, answersToSave)
                _event.value = OpenTicketEvent.OpenRegistrationScreenSuccessfully
            } catch (e: UnauthorizedLoginException) {
                _event.value = OpenTicketEvent.OpenLoginScreen
            } catch (e: Exception) {
                _event.value =
                    OpenTicketEvent.OpenFailedRegistrationScreen(ticketTitle, answersToSave)
            }
        }
    }
}

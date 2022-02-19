package com.glpi.ifsp.hortolandia.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.glpi.ifsp.hortolandia.domain.GetTicketsUseCase
import com.glpi.ifsp.hortolandia.domain.SessionUseCase
import com.glpi.ifsp.hortolandia.infrastructure.exceptions.UnauthorizedLoginException
import com.glpi.ifsp.hortolandia.ui.event.TicketEvent
import com.glpi.ifsp.hortolandia.ui.model.TicketUI
import com.hadilq.liveevent.LiveEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

class TicketViewModel(
    private val getTicketsUseCase: GetTicketsUseCase,
    private val sessionUseCase: SessionUseCase
) : ViewModel() {

    private var _ticketFlow: Flow<PagingData<TicketUI>> = emptyFlow()
    val ticketFlow: Flow<PagingData<TicketUI>>
        get() = _ticketFlow

    private var _event = LiveEvent<TicketEvent>()
    val event: LiveData<TicketEvent>
        get() = _event

    fun onStart() {
        try {
            _ticketFlow = getTicketsUseCase().cachedIn(viewModelScope)
        } catch (e: UnauthorizedLoginException) {
            sessionUseCase.clearSessionData()
            _event.value = TicketEvent.OpenLoginScreen
        } catch (e: Exception) {
            _event.value = TicketEvent.ShowInternalError
        }
    }
}

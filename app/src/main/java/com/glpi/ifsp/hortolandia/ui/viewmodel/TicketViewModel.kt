package com.glpi.ifsp.hortolandia.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.glpi.ifsp.hortolandia.data.model.Ticket
import com.glpi.ifsp.hortolandia.domain.GetTicketsUseCase
import com.glpi.ifsp.hortolandia.infrastructure.exceptions.InternalErrorException
import com.glpi.ifsp.hortolandia.ui.event.TicketEvent
import com.hadilq.liveevent.LiveEvent
import kotlinx.coroutines.flow.Flow

class TicketViewModel(
    private val getTicketsUseCase: GetTicketsUseCase
) : ViewModel() {

    private lateinit var _ticketFlow: Flow<PagingData<Ticket>>
    val ticketFlow: Flow<PagingData<Ticket>>
        get() = _ticketFlow

    private var _event = LiveEvent<TicketEvent>()
    val event: LiveData<TicketEvent>
        get() = _event

    fun onStart() {
        try {
            _ticketFlow = getTicketsUseCase().cachedIn(viewModelScope)
        } catch (e: InternalErrorException) {
            _event.value = TicketEvent.ShowInternalError
        }
    }
}

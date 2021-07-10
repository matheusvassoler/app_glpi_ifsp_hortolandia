package com.glpi.ifsp.hortolandia.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.glpi.ifsp.hortolandia.data.model.Ticket
import com.glpi.ifsp.hortolandia.domain.GetTicketsUseCase
import kotlinx.coroutines.flow.Flow

class TicketViewModel(
    private val getTicketsUseCase: GetTicketsUseCase
) : ViewModel() {

    private lateinit var _ticketFlow: Flow<PagingData<Ticket>>
    val ticketFlow: Flow<PagingData<Ticket>>
        get() = _ticketFlow

    fun onStart() {
        _ticketFlow = getTicketsUseCase().cachedIn(viewModelScope)
    }
}

package com.glpi.ifsp.hortolandia.data.repository.ticket

import androidx.paging.PagingData
import com.glpi.ifsp.hortolandia.data.model.Ticket
import kotlinx.coroutines.flow.Flow

interface TicketRepository {
    fun getTickets(sessionToken: String): Flow<PagingData<Ticket>>
}

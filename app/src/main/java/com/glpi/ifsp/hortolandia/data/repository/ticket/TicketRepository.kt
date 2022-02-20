package com.glpi.ifsp.hortolandia.data.repository.ticket

import androidx.paging.PagingData
import com.glpi.ifsp.hortolandia.data.model.Ticket
import com.glpi.ifsp.hortolandia.data.model.TicketInput
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface TicketRepository {
    fun getTickets(sessionToken: String): Flow<PagingData<Ticket>>
    suspend fun createTicket(sessionToken: String, ticketInput: TicketInput): Response<Void>
}

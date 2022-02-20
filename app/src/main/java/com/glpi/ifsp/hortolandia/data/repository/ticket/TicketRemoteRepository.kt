package com.glpi.ifsp.hortolandia.data.repository.ticket

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.glpi.ifsp.hortolandia.data.model.Ticket
import com.glpi.ifsp.hortolandia.data.model.TicketInput
import com.glpi.ifsp.hortolandia.data.source.remote.ApiClient
import com.glpi.ifsp.hortolandia.data.source.remote.ticketpaging.TicketSource
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

class TicketRemoteRepository(private val api: ApiClient) : TicketRepository {

    override fun getTickets(sessionToken: String): Flow<PagingData<Ticket>> {
        return Pager(PagingConfig(pageSize = NUMBER_OF_ELEMENTS_PER_PAGER)) {
            TicketSource(api, sessionToken)
        }.flow
    }

    override suspend fun createTicket(sessionToken: String, ticketInput: TicketInput): Response<Void> {
        return api().createTicket(sessionToken, ticketInput)
    }

    companion object {
        private const val NUMBER_OF_ELEMENTS_PER_PAGER = 16
    }
}

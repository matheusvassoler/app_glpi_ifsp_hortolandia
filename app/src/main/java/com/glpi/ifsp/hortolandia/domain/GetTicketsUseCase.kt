package com.glpi.ifsp.hortolandia.domain

import androidx.paging.PagingData
import com.glpi.ifsp.hortolandia.data.model.Ticket
import com.glpi.ifsp.hortolandia.data.repository.ticket.TicketRepository
import com.glpi.ifsp.hortolandia.infrastructure.exceptions.InternalErrorException
import kotlinx.coroutines.flow.Flow

class GetTicketsUseCase(
    private val ticketRepository: TicketRepository,
    private val sessionUseCase: SessionUseCase
) {

    operator fun invoke(): Flow<PagingData<Ticket>> {
        val sessionToken = sessionUseCase.getSessionToken() ?: throw InternalErrorException()

        return ticketRepository.getTickets(sessionToken)
    }
}

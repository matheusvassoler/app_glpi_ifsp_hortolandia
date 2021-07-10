package com.glpi.ifsp.hortolandia.domain

import androidx.paging.PagingData
import com.glpi.ifsp.hortolandia.BaseUnitTest
import com.glpi.ifsp.hortolandia.data.model.Ticket
import com.glpi.ifsp.hortolandia.data.repository.ticket.TicketRepository
import com.glpi.ifsp.hortolandia.infrastructure.exceptions.InternalErrorException
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Test

class GetTicketsUseCaseTest : BaseUnitTest() {

    @RelaxedMockK
    private lateinit var ticketRepository: TicketRepository

    @RelaxedMockK
    private lateinit var sessionUseCase: SessionUseCase

    @InjectMockKs
    private lateinit var getTicketsUseCase: GetTicketsUseCase

    @Test
    fun `UseCase SHOULD call TicketRepository to get Tickets`() = runBlocking {
        // GIVEN
        coEvery { sessionUseCase.getSessionToken() } returns "12345"
        val flow = flow {
            emit(PagingData.from(arrayListOf(Ticket("", "", "", 0))))
        }
        coEvery { ticketRepository.getTickets("12345") } returns flow
        // WHEN
        val result = getTicketsUseCase()
        // THEN
        verify { ticketRepository.getTickets("12345") }
        assertThat(result).isEqualTo(flow)
    }

    @Test(expected = InternalErrorException::class)
    fun `UseCase SHOULD throw InternalErrorException WHEN session token is null`(): Unit = runBlocking {
        // GIVEN
        coEvery { sessionUseCase.getSessionToken() } returns null

        // WHEN
        getTicketsUseCase()
    }
}

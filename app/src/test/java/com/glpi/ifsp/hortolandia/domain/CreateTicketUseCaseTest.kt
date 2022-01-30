package com.glpi.ifsp.hortolandia.domain

import com.glpi.ifsp.hortolandia.BaseUnitTest
import com.glpi.ifsp.hortolandia.data.model.TicketData
import com.glpi.ifsp.hortolandia.data.model.TicketInput
import com.glpi.ifsp.hortolandia.data.repository.ticket.TicketRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.runBlocking
import org.junit.Test
import retrofit2.Response

class CreateTicketUseCaseTest : BaseUnitTest() {

    @RelaxedMockK
    private lateinit var sessionUseCase: SessionUseCase

    @RelaxedMockK
    private lateinit var ticketRepository: TicketRepository

    @InjectMockKs
    private lateinit var createTicketUseCase: CreateTicketUseCase

    @Test
    fun teste() = runBlocking {
        every { sessionUseCase.getSessionToken() } returns "12345"

        coEvery { ticketRepository.createTicket(any(), any()) } returns Response.success(null)

        val answersToSave = mutableMapOf("Questao 1" to "Resposta 1", "Questao 2" to "Resposta 2")
        val result = createTicketUseCase("Teste formulario", answersToSave)

        val ticketInput = TicketInput(
            ticketData = TicketData(
                name = "Teste formulario",
                content = "Questao 1: Resposta 1\nQuestao 2: Resposta 2",
                status = 1,
                urgency = 1
            )
        )
        coVerify { ticketRepository.createTicket(any(), ticketInput) }
    }
}

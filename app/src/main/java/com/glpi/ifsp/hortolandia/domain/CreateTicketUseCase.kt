package com.glpi.ifsp.hortolandia.domain

import android.os.Build
import com.glpi.ifsp.hortolandia.data.model.TicketData
import com.glpi.ifsp.hortolandia.data.model.TicketInput
import com.glpi.ifsp.hortolandia.data.repository.ticket.TicketRepository
import com.glpi.ifsp.hortolandia.infrastructure.Constant
import com.glpi.ifsp.hortolandia.infrastructure.exceptions.InternalErrorException
import com.glpi.ifsp.hortolandia.infrastructure.exceptions.ResponseRequestException
import com.glpi.ifsp.hortolandia.infrastructure.exceptions.UnauthorizedLoginException
import java.util.stream.Collectors
import retrofit2.Response

class CreateTicketUseCase(
    private val ticketRepository: TicketRepository,
    private val sessionUseCase: SessionUseCase
) {

    suspend operator fun invoke(ticketTitle: String, answersToSave: Map<String, String>) {
        val sessionToken = sessionUseCase.getSessionToken() ?: throw InternalErrorException()

        val ticketInput = TicketInput(
            TicketData(
                name = ticketTitle,
                content = getContent(answersToSave),
                status = 1,
                urgency = 1
            )
        )

        val response = ticketRepository.createTicket(sessionToken, ticketInput)

        checkIfThrowException(response)
    }

    private fun checkIfThrowException(response: Response<Void>) {
        if (response.code() != Constant.RequestStatusCode.CREATED) {
            when {
                response.code() == Constant.RequestStatusCode.UNAUTHORIZED
                        || response.code() == Constant.RequestStatusCode.FORBIDDEN -> {
                    throw UnauthorizedLoginException()
                }
                else -> {
                    throw ResponseRequestException()
                }
            }
        }
    }

    private fun getContent(answersToSave: Map<String, String>): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            answersToSave.entries.stream().map { entrySet ->
                entrySet.key + ": " + entrySet.value
            }.collect(Collectors.joining("\n"))
        } else {
            TODO("Subir a versao do app em breve")
        }
    }
}

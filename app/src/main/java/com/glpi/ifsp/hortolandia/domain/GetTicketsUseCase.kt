package com.glpi.ifsp.hortolandia.domain

import androidx.paging.PagingData
import androidx.paging.map
import com.glpi.ifsp.hortolandia.data.model.Ticket
import com.glpi.ifsp.hortolandia.data.repository.ticket.TicketRepository
import com.glpi.ifsp.hortolandia.infrastructure.exceptions.InternalErrorException
import java.text.SimpleDateFormat
import java.util.Locale
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetTicketsUseCase(
    private val ticketRepository: TicketRepository,
    private val sessionUseCase: SessionUseCase
) {

    operator fun invoke(): Flow<PagingData<Ticket>> {
        val sessionToken = sessionUseCase.getSessionToken() ?: throw InternalErrorException()

        return ticketRepository.getTickets(sessionToken).map { pagingData ->
            pagingData.map { ticket ->
                val content = removeHtmlCodeFromTicketDescription(ticket.content)
                val updateDate = formatAmericanDateToBrazil(ticket.updateDate)
                ticket.run {
                    Ticket(id, title, content, updateDate, status, openingDate)
                }
            }
        }
    }

    private fun removeHtmlCodeFromTicketDescription(content: String) =
        content.replace(HTML_START_PARAGRAPH_TAG, "").replace(HTML_END_PARAGRAPH_TAG, "")

    private fun formatAmericanDateToBrazil(date: String): String {
        val brLocale = Locale("pt", "BR")
        val currentDateFormat = SimpleDateFormat(AMERICAN_FULL_FORMAT, brLocale)
        val formattedDate = currentDateFormat.parse(date)

        val expectedDateFormat = SimpleDateFormat(BRAZILIAN_FORMAT, brLocale)
        return if (formattedDate != null) {
            expectedDateFormat.format(formattedDate)
        } else {
            date
        }
    }

    companion object {
        private const val HTML_START_PARAGRAPH_TAG = "&lt;p&gt;"
        private const val HTML_END_PARAGRAPH_TAG = "&lt;/p&gt;"
        private const val AMERICAN_FULL_FORMAT = "yyyy-MM-dd hh:mm:ss"
        private const val BRAZILIAN_FORMAT = "dd/MM/yyyy"
    }
}

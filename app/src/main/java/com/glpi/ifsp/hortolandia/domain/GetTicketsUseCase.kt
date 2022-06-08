package com.glpi.ifsp.hortolandia.domain

import androidx.paging.PagingData
import androidx.paging.map
import com.glpi.ifsp.hortolandia.data.model.Ticket
import com.glpi.ifsp.hortolandia.data.repository.ticket.TicketRepository
import com.glpi.ifsp.hortolandia.gateway.LabelGateway
import com.glpi.ifsp.hortolandia.infrastructure.exceptions.InternalErrorException
import com.glpi.ifsp.hortolandia.infrastructure.utils.removeUnicodeHtmlFromText
import com.glpi.ifsp.hortolandia.ui.model.TicketUI
import java.text.SimpleDateFormat
import java.util.Locale
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetTicketsUseCase(
    private val ticketRepository: TicketRepository,
    private val sessionUseCase: SessionUseCase,
    private val labelGateway: LabelGateway
) {

    operator fun invoke(): Flow<PagingData<TicketUI>> {
        val sessionToken = sessionUseCase.getSessionToken() ?: throw InternalErrorException()

        return ticketRepository.getTickets(sessionToken).map { pagingData ->
            pagingData.map { ticket ->
                convertTicketToTicketUI(ticket)
            }
        }
    }

    private fun convertTicketToTicketUI(ticket: Ticket): TicketUI {
        val (updateDate, updateHour) = formatAmericanDateToBrazil(ticket.updateDate)
        val (openingDate, openingHour) = formatAmericanDateToBrazil(ticket.openingDate)
        val status = mapIntStatusToString(ticket.status)
        return ticket.run {
            TicketUI(
                id = id.toString(),
                title = title,
                description = removeUnicodeHtmlFromText(ticket.content),
                openingDate = openingDate,
                openingHour = openingHour,
                updateDate = updateDate,
                updateHour = updateHour,
                status = status,
                percentageStatusProgress = getPercentageStatusProgress(this.status)
            )
        }
    }

    private fun formatAmericanDateToBrazil(date: String): Pair<String, String> {
        val brLocale = Locale(PT_LANGUAGE, BR_COUNTRY)
        val currentDateFormat = SimpleDateFormat(AMERICAN_FULL_FORMAT, brLocale)
        val formattedDate = currentDateFormat.parse(date)

        val expectedDateFormat = SimpleDateFormat(BRAZILIAN_DATE_FORMAT, brLocale)
        val expectedHourFormat = SimpleDateFormat(BRAZILIAN_HOUR_FORMAT, brLocale)

        val resultDate = expectedDateFormat.format(formattedDate)
        val resultHour = expectedHourFormat.format(formattedDate)

        return Pair(resultDate, resultHour)
    }

    private fun mapIntStatusToString(status: Int): String {
        return when (status) {
            STATUS_NEW -> labelGateway.getLabelForStatusNew()
            STATUS_PROCESSING -> labelGateway.getLabelForStatusProcessing()
            STATUS_PENDING -> labelGateway.getLabelForStatusPending()
            STATUS_SOLVED -> labelGateway.getLabelForStatusSolved()
            STATUS_CLOSED -> labelGateway.getLabelForStatusClosed()
            else -> ""
        }
    }

    private fun getPercentageStatusProgress(status: Int): Int {
        return when (status) {
            STATUS_NEW -> ZERO_PERCENT
            STATUS_PROCESSING -> TWENTY_FIVE_PERCENT
            STATUS_PENDING -> FIFTY_PERCENT
            STATUS_SOLVED -> SEVENTY_FIVE_PERCENT
            STATUS_CLOSED -> ONE_HUNDRED_PERCENT
            else -> ZERO_PERCENT
        }
    }

    companion object {
        private const val HTML_START_PARAGRAPH_TAG = "&lt;p&gt;"
        private const val HTML_END_PARAGRAPH_TAG = "&lt;/p&gt;"
        private const val AMERICAN_FULL_FORMAT = "yyyy-MM-dd HH:mm:ss"
        private const val BRAZILIAN_DATE_FORMAT = "dd/MM/yyyy"
        private const val BRAZILIAN_HOUR_FORMAT = "HH:mm"
        private const val STATUS_NEW = 1
        private const val STATUS_PROCESSING = 2
        private const val STATUS_PENDING = 3
        private const val STATUS_SOLVED = 4
        private const val STATUS_CLOSED = 5
        private const val PT_LANGUAGE = "pt"
        private const val BR_COUNTRY = "BR"
        private const val ZERO_PERCENT = 0
        private const val TWENTY_FIVE_PERCENT = 25
        private const val FIFTY_PERCENT = 50
        private const val SEVENTY_FIVE_PERCENT = 75
        private const val ONE_HUNDRED_PERCENT = 100
    }
}

package com.glpi.ifsp.hortolandia.data.source.remote.ticketpaging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.glpi.ifsp.hortolandia.data.model.Ticket
import com.glpi.ifsp.hortolandia.data.source.remote.ApiClient
import com.glpi.ifsp.hortolandia.infrastructure.Constant
import com.glpi.ifsp.hortolandia.infrastructure.exceptions.UnauthorizedLoginException

class TicketSource(
    private val api: ApiClient,
    private val sessionToken: String
) : PagingSource<String, Ticket>() {
    override suspend fun load(params: LoadParams<String>): LoadResult<String, Ticket> {
        return try {
            val pageNumber = params.key ?: INITIAL_RANGE
            val response = api().getTickets(sessionToken, pageNumber)

            if (response.code() == Constant.RequestStatusCode.UNAUTHORIZED ||
                response.code() == Constant.RequestStatusCode.FORBIDDEN) {
                throw UnauthorizedLoginException()
            }

            val data: List<Ticket>? = response.body()
            val currentRange: String = response.headers()[RANGE_HEADER_NAME].orEmpty()

            val nextRange = getNextRange(currentRange)

            LoadResult.Page(
                data = data.orEmpty(),
                prevKey = null,
                nextKey = nextRange
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<String, Ticket>): String? {
        return null
    }

    private fun getNextRange(currentRange: String): String? {
        var nextRange: String? = null
        if (currentRange.isNotEmpty()) {
            val lastIndexOfTheRange = getLastIndexOfTheRange(currentRange)
            val totalTickets = getTotalTickets(currentRange)

            if (checkIfThereIsRangeToLoad(totalTickets, lastIndexOfTheRange)) {
                val newFirstIndexOfRange = lastIndexOfTheRange.toInt() + 1
                val newLastIndexOfRange = lastIndexOfTheRange.toInt() + NUMBER_OF_INDICES_FOR_NEXT_RANGE
                nextRange = "$newFirstIndexOfRange-$newLastIndexOfRange"
            }
        }
        return nextRange
    }

    private fun checkIfThereIsRangeToLoad(totalTickets: String, lastIndexOfTheRange: String): Boolean =
        lastIndexOfTheRange.toInt() < lastIndexOfTicketList(totalTickets)

    private fun lastIndexOfTicketList(totalTickets: String) = totalTickets.toInt() - 1

    private fun getLastIndexOfTheRange(availableRange: String): String =
        availableRange.split("-")[1].split("/")[0]

    private fun getTotalTickets(availableRange: String): String =
        availableRange.split("-")[1].split("/")[1]

    companion object {
        private const val INITIAL_RANGE = "0-15"
        private const val RANGE_HEADER_NAME = "Content-Range"
        private const val NUMBER_OF_INDICES_FOR_NEXT_RANGE = 16
    }
}

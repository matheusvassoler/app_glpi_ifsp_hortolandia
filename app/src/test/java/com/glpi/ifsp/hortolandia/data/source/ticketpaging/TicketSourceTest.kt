package com.glpi.ifsp.hortolandia.data.source.ticketpaging

import androidx.paging.PagingSource
import com.glpi.ifsp.hortolandia.BaseUnitTest
import com.glpi.ifsp.hortolandia.data.model.Ticket
import com.glpi.ifsp.hortolandia.data.source.remote.ApiClient
import com.glpi.ifsp.hortolandia.data.source.remote.ticketpaging.TicketSource
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.runBlocking
import okhttp3.Headers
import org.junit.Test
import retrofit2.Response

class TicketSourceTest : BaseUnitTest() {

    @RelaxedMockK
    private lateinit var api: ApiClient

    @Test
    fun `load SHOULD get tickets and SET 16 indices to range for nextKey getting value 4-19 WHEN first range is 0-3 and has 10 elements`() = runBlocking {
        // GIVEN
        coEvery { api().getTickets(any(), any()) } returns mockResponse("0-3/10")
        val pagingSource = TicketSource(api, "")
        // WHEN
        val loadResult = pagingSource.load(PagingSource.LoadParams.Refresh(null, 2, true))
        // THEN
        assertThat(loadResult).isEqualTo(
            PagingSource.LoadResult.Page(
                data = listOf(Ticket("", "", "", "", 0)),
                prevKey = null,
                nextKey = "4-19"
            )
        )
    }

    @Test
    fun `load SHOULD get tickets and SET 16 indices to range for nextKey getting value 26-41 WHEN current range is 10-25 and has 40 elements in the list`() = runBlocking {
        // GIVEN
        coEvery { api().getTickets(any(), any()) } returns mockResponse("10-25/40")
        val pagingSource = TicketSource(api, "")
        // WHEN
        val loadResult = pagingSource.load(PagingSource.LoadParams.Refresh(null, 2, true))
        // THEN
        assertThat(loadResult).isEqualTo(
            PagingSource.LoadResult.Page(
                data = listOf(Ticket("", "", "", "", 0)),
                prevKey = null,
                nextKey = "26-41"
            )
        )
    }

    @Test
    fun `load SHOULD get ticket and SET null value to nextKey WHEN has just one ticket in the list`() = runBlocking {
        // GIVEN
        coEvery { api().getTickets(any(), any()) } returns mockResponse("0-0/1")
        val pagingSource = TicketSource(api, "")
        // WHEN
        val loadResult = pagingSource.load(PagingSource.LoadParams.Refresh(null, 2, true))
        // THEN
        assertThat(loadResult).isEqualTo(
            PagingSource.LoadResult.Page(
                data = listOf(Ticket("", "", "", "", 0)),
                prevKey = null,
                nextKey = null
            )
        )
    }

    @Test
    fun `load SHOULD return empty list and SET null value to nextKey WHEN does not has ticket in the list`() = runBlocking {
        // GIVEN
        coEvery { api().getTickets(any(), any()) } returns Response.success(listOf())
        val pagingSource = TicketSource(api, "")
        // WHEN
        val loadResult = pagingSource.load(PagingSource.LoadParams.Refresh(null, 2, true))
        // THEN
        assertThat(loadResult).isEqualTo(
            PagingSource.LoadResult.Page(
                data = listOf(),
                prevKey = null,
                nextKey = null
            )
        )
    }

    @Test
    fun `load SHOULD return empty list and SET null value to nextKey WHEN data is null`() = runBlocking {
        // GIVEN
        coEvery { api().getTickets(any(), any()) } returns Response.success(null)
        val pagingSource = TicketSource(api, "")
        // WHEN
        val loadResult = pagingSource.load(PagingSource.LoadParams.Refresh(null, 2, true))
        // THEN
        assertThat(loadResult).isEqualTo(
            PagingSource.LoadResult.Page(
                data = listOf(),
                prevKey = null,
                nextKey = null
            )
        )
    }

    private fun mockResponse(range: String): Response<List<Ticket>> {
        return Response.success(
            listOf(Ticket("", "", "", "", 0)),
            Headers.headersOf("Content-Range ", range)
        )
    }
}

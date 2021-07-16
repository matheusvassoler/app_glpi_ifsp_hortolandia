package com.glpi.ifsp.hortolandia.data.repository.ticket

import androidx.paging.AsyncPagingDataDiffer
import com.glpi.ifsp.hortolandia.BaseUnitTest
import com.glpi.ifsp.hortolandia.data.model.Ticket
import com.glpi.ifsp.hortolandia.data.source.remote.ApiClient
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Response

@ExperimentalCoroutinesApi
class TicketRemoteRepositoryTest : BaseUnitTest() {

    @RelaxedMockK
    private lateinit var api: ApiClient

    @InjectMockKs
    private lateinit var repository: TicketRemoteRepository

    private val testDispatcher = TestCoroutineDispatcher()
    private val testScope = TestCoroutineScope(testDispatcher)

    @Before
    override fun setup() {
        super.setup()
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getTickets SHOULD call method from API and return a list WITH tickets`() {
        // GIVEN
        val expectedResult = listOf(Ticket("1", "Ticket1", "teste", "13/09/2020", 0, "12/09/2020"))
        coEvery { api().getTickets("12345", "0-15") } returns mockResponse()
        val list = AsyncPagingDataDiffer(
            diffCallback = MyDiffCallback(),
            updateCallback = NoopListCallback()
        )
        // WHEN
        val flow = repository.getTickets("12345")
        testScope.launch {
            list.submitData(flow.first())
        }
        // THEN
        coVerify { api().getTickets("12345", "0-15") }
        assertThat(list.snapshot().items).isEqualTo(expectedResult)
    }

    private fun mockResponse(): Response<List<Ticket>> {
        return Response.success(listOf(Ticket("1", "Ticket1", "teste", "13/09/2020", 0, "12/09/2020")))
    }
}

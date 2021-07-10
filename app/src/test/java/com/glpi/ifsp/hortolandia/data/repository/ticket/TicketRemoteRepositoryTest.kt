package com.glpi.ifsp.hortolandia.data.repository.ticket

import androidx.paging.AsyncPagingDataDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import com.glpi.ifsp.hortolandia.data.model.Ticket
import com.glpi.ifsp.hortolandia.data.source.remote.ApiClient
import com.google.common.truth.Truth.assertThat
import io.mockk.MockKAnnotations
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
class TicketRemoteRepositoryTest {

    @RelaxedMockK
    private lateinit var api: ApiClient

    @InjectMockKs
    private lateinit var repository: TicketRemoteRepository

    private val testDispatcher = TestCoroutineDispatcher()
    private val testScope = TestCoroutineScope(testDispatcher)

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getTickets SHOULD call method from API and return a list WITH tickets`() {
        // GIVEN
        val expectedResult = listOf(Ticket("Ticket1", "teste", "13/09/2020", 0))
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
        return Response.success(listOf(Ticket("Ticket1", "teste", "13/09/2020", 0)))
    }

    class MyDiffCallback : DiffUtil.ItemCallback<Ticket>() {
        override fun areItemsTheSame(oldItem: Ticket, newItem: Ticket): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Ticket, newItem: Ticket): Boolean {
            return oldItem == newItem
        }
    }

    class NoopListCallback : ListUpdateCallback {
        override fun onChanged(position: Int, count: Int, payload: Any?) {}
        override fun onMoved(fromPosition: Int, toPosition: Int) {}
        override fun onInserted(position: Int, count: Int) {}
        override fun onRemoved(position: Int, count: Int) {}
    }
}

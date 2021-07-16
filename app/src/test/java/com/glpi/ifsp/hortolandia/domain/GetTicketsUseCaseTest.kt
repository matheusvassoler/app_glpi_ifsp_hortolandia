package com.glpi.ifsp.hortolandia.domain

import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import com.glpi.ifsp.hortolandia.BaseUnitTest
import com.glpi.ifsp.hortolandia.data.model.Ticket
import com.glpi.ifsp.hortolandia.data.repository.ticket.TicketRepository
import com.glpi.ifsp.hortolandia.infrastructure.exceptions.InternalErrorException
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class GetTicketsUseCaseTest : BaseUnitTest() {

    @RelaxedMockK
    private lateinit var ticketRepository: TicketRepository

    @RelaxedMockK
    private lateinit var sessionUseCase: SessionUseCase

    @InjectMockKs
    private lateinit var getTicketsUseCase: GetTicketsUseCase

    private val testDispatcher = TestCoroutineDispatcher()

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
    fun `UseCase SHOULD call TicketRepository to get Tickets AND format the date for Brazil AND do nothing with the description when it does not has html tag inside`() = testDispatcher.runBlockingTest {
        // GIVEN
        val expectedList = listOf(Ticket("1", "Ticket 1", "Descrição ticket 1", "08/06/2021", 0, "07/06/2021"))

        coEvery { sessionUseCase.getSessionToken() } returns "12345"

        val flow = flow {
            emit(PagingData.from(arrayListOf(Ticket("1", "Ticket 1", "Descrição ticket 1", "2021-06-08 22:01:31", 0, "07/06/2021"))))
        }

        coEvery { ticketRepository.getTickets("12345") } returns flow

        val resultList = AsyncPagingDataDiffer(
            diffCallback = MyDiffCallback(),
            updateCallback = NoopListCallback()
        )
        // WHEN
        val result = getTicketsUseCase()

        result.collectLatest { pagingData ->
            resultList.submitData(pagingData)
        }
        // THEN
        assertThat(resultList.snapshot().items).isEqualTo(expectedList)
    }

    @Test
    fun `UseCase SHOULD call TicketRepository to get Tickets AND format the date for brazil AND remove the html tag from the description`() = testDispatcher.runBlockingTest {
        // GIVEN
        val expectedList = listOf(Ticket("1", "Ticket 1", "Descrição ticket 1", "08/06/2021", 0, "07/06/2021"))

        coEvery { sessionUseCase.getSessionToken() } returns "12345"

        val flow = flow {
            emit(PagingData.from(arrayListOf(Ticket("1", "Ticket 1", "&lt;p&gt;Descrição ticket 1&lt;/p&gt;", "2021-06-08 22:01:31", 0, "07/06/2021"))))
        }

        coEvery { ticketRepository.getTickets("12345") } returns flow

        val resultList = AsyncPagingDataDiffer(
            diffCallback = MyDiffCallback(),
            updateCallback = NoopListCallback()
        )
        // WHEN
        val result = getTicketsUseCase()

        result.collectLatest { pagingData ->
            resultList.submitData(pagingData)
        }
        // THEN
        assertThat(resultList.snapshot().items).isEqualTo(expectedList)
    }

    @Test(expected = InternalErrorException::class)
    fun `UseCase SHOULD throw InternalErrorException WHEN session token is null`(): Unit = runBlocking {
        // GIVEN
        coEvery { sessionUseCase.getSessionToken() } returns null

        // WHEN
        getTicketsUseCase()
    }
}

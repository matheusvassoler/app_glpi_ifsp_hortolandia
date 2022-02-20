package com.glpi.ifsp.hortolandia.domain

import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import com.glpi.ifsp.hortolandia.BaseUnitTest
import com.glpi.ifsp.hortolandia.data.model.Ticket
import com.glpi.ifsp.hortolandia.data.repository.ticket.TicketRepository
import com.glpi.ifsp.hortolandia.gateway.LabelGateway
import com.glpi.ifsp.hortolandia.infrastructure.exceptions.InternalErrorException
import com.glpi.ifsp.hortolandia.ui.model.TicketUI
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.every
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

    @RelaxedMockK
    private lateinit var labelGateway: LabelGateway

    @InjectMockKs
    private lateinit var getTicketsUseCase: GetTicketsUseCase

    private val testDispatcher = TestCoroutineDispatcher()

    @Before
    override fun setup() {
        super.setup()
        Dispatchers.setMain(testDispatcher)
        every { labelGateway.getLabelForStatusNew() } returns "novo"
        every { labelGateway.getLabelForStatusProcessing() } returns "processando"
        every { labelGateway.getLabelForStatusPending() } returns "pendente"
        every { labelGateway.getLabelForStatusSolved() } returns "solucionado"
        every { labelGateway.getLabelForStatusClosed() } returns "fechado"
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `UseCase SHOULD call TicketRepository to get Tickets AND returns TicketUI with formatted date and hour (opening and update) for Brazil AND do nothing with the description when it does not has html tag inside`() = testDispatcher.runBlockingTest {
        // GIVEN
        val expectedList = listOf(TicketUI("1", "Ticket 1", "Descrição ticket 1", "07/06/2021", "11:06", "08/06/2021", "22:01", "novo", 0))

        coEvery { sessionUseCase.getSessionToken() } returns "12345"

        val flow = flow {
            emit(PagingData.from(arrayListOf(Ticket(1, "Ticket 1", "Descrição ticket 1", "2021-06-08 22:01:31", 1, "2021-06-07 11:06:31"))))
        }

        coEvery { ticketRepository.getTickets("12345") } returns flow

        val resultList = AsyncPagingDataDiffer(
            diffCallback = MyDiffCallback<TicketUI>(),
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
    fun `UseCase SHOULD call TicketRepository to get Tickets AND returns TicketUI with formatted date and hour (opening and update) for brazil AND remove the html tag from the description`() = testDispatcher.runBlockingTest {
        // GIVEN
        val expectedList = listOf(TicketUI("1", "Ticket 1", "Descrição ticket 1", "07/06/2021", "11:06", "08/06/2021", "22:01", "novo", 0))

        coEvery { sessionUseCase.getSessionToken() } returns "12345"

        val flow = flow {
            emit(PagingData.from(arrayListOf(Ticket(1, "Ticket 1", "&lt;p&gt;Descrição ticket 1&lt;/p&gt;", "2021-06-08 22:01:31", 1, "2021-06-07 11:06:31"))))
        }

        coEvery { ticketRepository.getTickets("12345") } returns flow

        val resultList = AsyncPagingDataDiffer(
            diffCallback = MyDiffCallback<TicketUI>(),
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
    fun `UseCase SHOULD call TicketRepository to get Tickets AND returns TicketUI WITH status new AND percentage status progress 0 WHEN status from Ticket is 1`() = testDispatcher.runBlockingTest {
        // GIVEN
        val expectedList = listOf(TicketUI("1", "Ticket 1", "Descrição ticket 1", "01/12/2021", "12:06", "13/12/2021", "00:01", "novo", 0))

        coEvery { sessionUseCase.getSessionToken() } returns "12345"

        val flow = flow {
            emit(PagingData.from(arrayListOf(Ticket(1, "Ticket 1", "&lt;p&gt;Descrição ticket 1&lt;/p&gt;", "2021-12-13 00:01:31", 1, "2021-12-01 12:06:31"))))
        }

        coEvery { ticketRepository.getTickets("12345") } returns flow

        val resultList = AsyncPagingDataDiffer(
            diffCallback = MyDiffCallback<TicketUI>(),
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
    fun `UseCase SHOULD call TicketRepository to get Tickets AND returns TicketUI WITH status processing AND percentage status progress 25 WHEN status from Ticket is 2`() = testDispatcher.runBlockingTest {
        // GIVEN
        val expectedList = listOf(TicketUI("1", "Ticket 1", "Descrição ticket 1", "07/06/2021", "11:06", "08/06/2021", "22:01", "processando", 25))

        coEvery { sessionUseCase.getSessionToken() } returns "12345"

        val flow = flow {
            emit(PagingData.from(arrayListOf(Ticket(1, "Ticket 1", "&lt;p&gt;Descrição ticket 1&lt;/p&gt;", "2021-06-08 22:01:31", 2, "2021-06-07 11:06:31"))))
        }

        coEvery { ticketRepository.getTickets("12345") } returns flow

        val resultList = AsyncPagingDataDiffer(
            diffCallback = MyDiffCallback<TicketUI>(),
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
    fun `UseCase SHOULD call TicketRepository to get Tickets AND returns TicketUI WITH status pending AND percentage status progress 50 WHEN status from Ticket is 3`() = testDispatcher.runBlockingTest {
        // GIVEN
        val expectedList = listOf(TicketUI("1", "Ticket 1", "Descrição ticket 1", "07/06/2021", "11:06", "08/06/2021", "22:01", "pendente", 50))

        coEvery { sessionUseCase.getSessionToken() } returns "12345"

        val flow = flow {
            emit(PagingData.from(arrayListOf(Ticket(1, "Ticket 1", "&lt;p&gt;Descrição ticket 1&lt;/p&gt;", "2021-06-08 22:01:31", 3, "2021-06-07 11:06:31"))))
        }

        coEvery { ticketRepository.getTickets("12345") } returns flow

        val resultList = AsyncPagingDataDiffer(
            diffCallback = MyDiffCallback<TicketUI>(),
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
    fun `UseCase SHOULD call TicketRepository to get Tickets AND returns TicketUI WITH status solved AND percentage status progress 75 WHEN status from Ticket is 4`() = testDispatcher.runBlockingTest {
        // GIVEN
        val expectedList = listOf(TicketUI("1", "Ticket 1", "Descrição ticket 1", "07/06/2021", "11:06", "08/06/2021", "22:01", "solucionado", 75))

        coEvery { sessionUseCase.getSessionToken() } returns "12345"

        val flow = flow {
            emit(PagingData.from(arrayListOf(Ticket(1, "Ticket 1", "&lt;p&gt;Descrição ticket 1&lt;/p&gt;", "2021-06-08 22:01:31", 4, "2021-06-07 11:06:31"))))
        }

        coEvery { ticketRepository.getTickets("12345") } returns flow

        val resultList = AsyncPagingDataDiffer(
            diffCallback = MyDiffCallback<TicketUI>(),
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
    fun `UseCase SHOULD call TicketRepository to get Tickets AND returns TicketUI WITH status closed AND percentage status progress 100 WHEN status from Ticket is 5`() = testDispatcher.runBlockingTest {
        // GIVEN
        val expectedList = listOf(TicketUI("1", "Ticket 1", "Descrição ticket 1", "07/06/2021", "11:06", "08/06/2021", "22:01", "fechado", 100))

        coEvery { sessionUseCase.getSessionToken() } returns "12345"

        val flow = flow {
            emit(PagingData.from(arrayListOf(Ticket(1, "Ticket 1", "&lt;p&gt;Descrição ticket 1&lt;/p&gt;", "2021-06-08 22:01:31", 5, "2021-06-07 11:06:31"))))
        }

        coEvery { ticketRepository.getTickets("12345") } returns flow

        val resultList = AsyncPagingDataDiffer(
            diffCallback = MyDiffCallback<TicketUI>(),
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
    fun `UseCase SHOULD call TicketRepository to get Tickets AND returns TicketUI WITH empty status AND percentage status progress 0 WHEN status from Ticket is differente of 1,2,3,4 or 5`() = testDispatcher.runBlockingTest {
        // GIVEN
        val expectedList = listOf(TicketUI("1", "Ticket 1", "Descrição ticket 1", "07/06/2021", "11:06", "08/06/2021", "22:01", "", 0))

        coEvery { sessionUseCase.getSessionToken() } returns "12345"

        val flow = flow {
            emit(PagingData.from(arrayListOf(Ticket(1, "Ticket 1", "&lt;p&gt;Descrição ticket 1&lt;/p&gt;", "2021-06-08 22:01:31", 0, "2021-06-07 11:06:31"))))
        }

        coEvery { ticketRepository.getTickets("12345") } returns flow

        val resultList = AsyncPagingDataDiffer(
            diffCallback = MyDiffCallback<TicketUI>(),
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

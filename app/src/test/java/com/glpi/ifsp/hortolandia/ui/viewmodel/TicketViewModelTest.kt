package com.glpi.ifsp.hortolandia.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import com.glpi.ifsp.hortolandia.BaseUnitTest
import com.glpi.ifsp.hortolandia.domain.GetTicketsUseCase
import com.glpi.ifsp.hortolandia.domain.SessionUseCase
import com.glpi.ifsp.hortolandia.infrastructure.exceptions.InternalErrorException
import com.glpi.ifsp.hortolandia.ui.event.TicketEvent
import com.glpi.ifsp.hortolandia.ui.model.TicketUI
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.slot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class TicketViewModelTest : BaseUnitTest() {

    @RelaxedMockK
    private lateinit var getTicketsUseCase: GetTicketsUseCase

    @RelaxedMockK
    private lateinit var sessionUseCase: SessionUseCase

    @InjectMockKs
    private lateinit var viewModel: TicketViewModel

    @RelaxedMockK
    lateinit var eventObserver: Observer<TicketEvent>

    private val eventSlot = slot<TicketEvent>()

    private val eventList: ArrayList<TicketEvent> = arrayListOf()

    private val testDispatcher = TestCoroutineDispatcher()
    private val testScope = TestCoroutineScope(testDispatcher)

    @get: Rule
    val rule = InstantTaskExecutorRule()

    @Before
    override fun setup() {
        super.setup()
        Dispatchers.setMain(testDispatcher)
        // must happen in this order so we can capture initial event
        every { eventObserver.onChanged(capture(eventSlot)) } answers { eventList.add(eventSlot.captured) }
        viewModel.event.observeForever(eventObserver)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `viewModel SHOULD extend ViewModel`() {
        assertThat(viewModel).isInstanceOf(ViewModel::class.java)
    }

    @Test
    fun `onStart SHOULD call UseCase and get ticket list`() = runBlocking {
        // GIVEN
        val expectedResult = listOf(TicketUI("0", "Ticket 1", "Descrição ticket 1", "08/06/2021", "12:45", "07/06/2021", "11h10", "novo", 0))
        val pagingData = PagingData.from(expectedResult)
        val ticketFlow = flow {
            emit(pagingData)
        }
        every { getTicketsUseCase() } returns ticketFlow
        val list = AsyncPagingDataDiffer(
            diffCallback = MyDiffCallback<TicketUI>(),
            updateCallback = NoopListCallback()
        )
        // WHEN
        viewModel.onStart()
        testScope.launch {
            list.submitData(viewModel.ticketFlow.first())
        }
        // THEN
        assertThat(list.snapshot().items).isEqualTo(expectedResult)
    }

    @Test
    fun `onStart SHOULD emit ShowInternalError WHEN Session Token in shared preferences is null`() {
        // GIVEN
        every { getTicketsUseCase() } throws InternalErrorException()
        // WHEN
        viewModel.onStart()
        // THEN
        assertThat(eventList.size).isEqualTo(1)
        assertThat(eventList[0]).isInstanceOf(TicketEvent.ShowInternalError::class.java)
    }
}

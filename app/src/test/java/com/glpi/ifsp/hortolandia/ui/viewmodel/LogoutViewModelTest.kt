package com.glpi.ifsp.hortolandia.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.glpi.ifsp.hortolandia.domain.LogoutUseCase
import com.glpi.ifsp.hortolandia.ui.event.LogoutEvent
import com.glpi.ifsp.hortolandia.ui.state.LogoutState
import com.google.common.truth.Truth.assertThat
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.slot
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class LogoutViewModelTest {

    @RelaxedMockK
    private lateinit var logoutUseCase: LogoutUseCase

    @RelaxedMockK
    lateinit var stateObserver: Observer<LogoutState>

    private val stateSlot = slot<LogoutState>()

    private val stateList: ArrayList<LogoutState> = arrayListOf()

    @RelaxedMockK
    lateinit var eventObserver: Observer<LogoutEvent>

    private val eventSlot = slot<LogoutEvent>()

    private val eventList: ArrayList<LogoutEvent> = arrayListOf()

    @InjectMockKs
    private lateinit var viewModel: LogoutViewModel

    @get: Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxed = true)

        // must happen in this order so we can capture initial state
        every { stateObserver.onChanged(capture(stateSlot)) } answers { stateList.add(stateSlot.captured) }
        viewModel.state.observeForever(stateObserver)

        // must happen in this order so we can capture initial event
        every { eventObserver.onChanged(capture(eventSlot)) } answers { eventList.add(eventSlot.captured) }
        viewModel.event.observeForever(eventObserver)
    }

    @Test
    fun `viewModel SHOULD extend ViewModel`() {
        assertThat(viewModel).isInstanceOf(ViewModel::class.java)
    }

    @Test
    fun `onLogoutClick SHOULD emit GoToLogin Event WHEN logout is successfully`() {
        // WHEN
        viewModel.onLogoutClick()

        // THEN
        assertThat(eventList.size).isEqualTo(1)
        assertThat(eventList[0]).isInstanceOf(LogoutEvent.GoToLogin::class.java)
    }

    @Test
    fun `onLogoutClick SHOULD emit ShowLogoutError Event WHEN logout not is successful`() {
        // GIVEN
        coEvery { logoutUseCase(any()) } throws Exception()

        // WHEN
        viewModel.onLogoutClick()

        // THEN
        assertThat(eventList.size).isEqualTo(1)
        assertThat(eventList[0]).isInstanceOf(LogoutEvent.ShowLogoutError::class.java)
    }

    @Test
    fun `onLogoutClick SHOULD emit ShowLoading and HideLoading state WHEN requisition is on going and after is finalized, respectively`() {
        // WHEN
        viewModel.onLogoutClick()

        // THEN
        assertThat(stateList.size).isEqualTo(2)
        assertThat(stateList[0]).isInstanceOf(LogoutState.ShowLoading::class.java)
        assertThat(stateList[1]).isInstanceOf(LogoutState.HideLoading::class.java)
    }
}

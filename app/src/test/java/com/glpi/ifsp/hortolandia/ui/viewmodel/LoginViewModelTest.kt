package com.glpi.ifsp.hortolandia.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.glpi.ifsp.hortolandia.domain.LoginUseCase
import com.glpi.ifsp.hortolandia.infrastructure.exceptions.ResponseRequestException
import com.glpi.ifsp.hortolandia.infrastructure.exceptions.UnauthorizedLoginException
import com.glpi.ifsp.hortolandia.ui.event.LoginEvent
import com.glpi.ifsp.hortolandia.ui.model.LoginUI
import com.glpi.ifsp.hortolandia.ui.state.LoginState
import com.google.common.truth.Truth.assertThat
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.slot
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class LoginViewModelTest {

    @MockK
    private lateinit var loginUseCase: LoginUseCase

    @RelaxedMockK
    lateinit var stateObserver: Observer<LoginState>

    private val stateSlot = slot<LoginState>()

    private val stateList: ArrayList<LoginState> = arrayListOf()

    @RelaxedMockK
    lateinit var eventObserver: Observer<LoginEvent>

    private val eventSlot = slot<LoginEvent>()

    private val eventList: ArrayList<LoginEvent> = arrayListOf()

    @InjectMockKs
    private lateinit var viewModel: LoginViewModel

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
    fun `onLoginClick SHOULD emit OpenHome Event WHEN login is successful`() {
        // WHEN
        viewModel.onLoginClick("matheus", "1234")
        // THEN
        assertThat(eventList.size).isEqualTo(1)
        assertThat(eventList[0]).isInstanceOf(LoginEvent.OpenHome::class.java)
    }

    @Test
    fun `onLoginClick SHOULD emit ShowBadCredentialError Event WHEN username or password are invalid`() {
        // GIVEN
        coEvery { loginUseCase.makeLogin(LoginUI("matheus", "1234")) } throws UnauthorizedLoginException()
        // WHEN
        viewModel.onLoginClick("matheus", "1234")
        // THEN
        assertThat(eventList.size).isEqualTo(1)
        assertThat(eventList[0]).isInstanceOf(LoginEvent.ShowBadCredentialError::class.java)
    }

    @Test
    fun `onLoginClick SHOULD emit ShowConnectionError Event WHEN has some problem with connection`() {
        // GIVEN
        coEvery { loginUseCase.makeLogin(LoginUI("matheus", "1234")) } throws ResponseRequestException()
        // WHEN
        viewModel.onLoginClick("matheus", "1234")
        // THEN
        assertThat(eventList.size).isEqualTo(1)
        assertThat(eventList[0]).isInstanceOf(LoginEvent.ShowConnectionError::class.java)
    }

    @Test
    fun `login SHOULD emit ShowLoading and HideLoading state WHEN requisition is on going and after is finalized, respectively`() {
        // WHEN
        viewModel.onLoginClick("matheus", "1234")
        // THEN
        assertThat(stateList.size).isEqualTo(3)
        assertThat(stateList[1]).isInstanceOf(LoginState.ShowLoading::class.java)
        assertThat(stateList[2]).isInstanceOf(LoginState.HideLoading::class.java)
    }

    @Test
    fun `validateFields SHOULD emit ValidateField State with isUsernameFieldEmpty and IsPasswordFieldEmpty as true WHEN username and password are empty`() {
        // WHEN
        viewModel.onLoginClick("", "")
        // THEN
        assertThat(stateList.size).isEqualTo(1)
        (stateList[0] as LoginState.ValidateField).apply {
            assertThat(isUsernameEmpty).isTrue()
            assertThat(isPasswordEmpty).isTrue()
        }
    }

    @Test
    fun `validateFields SHOULD emit ValidateField State with isUsernameFieldEmpty and IsPasswordFieldEmpty as false WHEN username and password are not empty`() {
        // WHEN
        viewModel.onLoginClick("matheus", "1234")
        // THEN
        assertThat(stateList.size).isEqualTo(3)
        (stateList[0] as LoginState.ValidateField).apply {
            assertThat(isUsernameEmpty).isFalse()
            assertThat(isPasswordEmpty).isFalse()
        }
    }

    @Test
    fun `validateFields SHOULD emit ValidateField State with isUsernameFieldEmpty true and IsPasswordFieldEmpty false WHEN username is empty and password not`() {
        // WHEN
        viewModel.onLoginClick("", "1234")
        // THEN
        assertThat(stateList.size).isEqualTo(1)
        (stateList[0] as LoginState.ValidateField).apply {
            assertThat(isUsernameEmpty).isTrue()
            assertThat(isPasswordEmpty).isFalse()
        }
    }

    @Test
    fun `validateFields SHOULD emit ValidateField State with isUsernameFieldEmpty false and IsPasswordFieldEmpty true WHEN username is filled and password is empty`() {
        // WHEN
        viewModel.onLoginClick("matheus", "")
        // THEN
        assertThat(stateList.size).isEqualTo(1)
        (stateList[0] as LoginState.ValidateField).apply {
            assertThat(isUsernameEmpty).isFalse()
            assertThat(isPasswordEmpty).isTrue()
        }
    }
}

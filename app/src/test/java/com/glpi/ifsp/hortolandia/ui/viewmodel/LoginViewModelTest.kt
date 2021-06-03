package com.glpi.ifsp.hortolandia.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.glpi.ifsp.hortolandia.BaseUnitTest
import com.glpi.ifsp.hortolandia.data.resource.Resource
import com.glpi.ifsp.hortolandia.domain.LoginUseCase
import com.glpi.ifsp.hortolandia.ui.LoginUI
import com.glpi.ifsp.hortolandia.ui.wrapper.Event
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Rule
import org.junit.Test

class LoginViewModelTest : BaseUnitTest() {

    @MockK
    private lateinit var loginUseCase: LoginUseCase

    private lateinit var loginViewModel: LoginViewModel

    @get: Rule
    val rule = InstantTaskExecutorRule()

    @Test
    fun `checkIfUsernameFieldIsEmpty - Verify that username field is empty`() {
        val usernameObserver = mockk<Observer<Boolean>>(relaxed = true)

        initViewModel()
        loginViewModel.isUsernameEmpty.observeForever(usernameObserver)
        loginViewModel.makeLogin("", PASSWORD)


        assertForLoginField(loginViewModel.isUsernameEmpty, usernameObserver, true)
        coVerify(exactly = 0) { loginUseCase.makeLogin(any()) }
    }

    private fun assertForLoginField(
        fieldLiveData: LiveData<Boolean>,
        fieldObserver: Observer<Boolean>,
        isFieldEmpty: Boolean
    ) {
        assertThat(fieldLiveData.value, equalTo(isFieldEmpty))
        verify { fieldObserver.onChanged(isFieldEmpty) }
    }

    @Test
    fun `checkIfUsernameFieldIsEmpty - Verify that username field is not empty`() {
        val usernameObserver = mockk<Observer<Boolean>>(relaxed = true)

        initViewModel()
        loginViewModel.isUsernameEmpty.observeForever(usernameObserver)
        loginViewModel.makeLogin(USERNAME, PASSWORD)

        assertForLoginField(loginViewModel.isUsernameEmpty, usernameObserver, false)
        coVerify(exactly = 1) { loginUseCase.makeLogin(any()) }
    }

    @Test
    fun `checkIfPasswordFieldIsEmpty - Verify that password field is empty`() {
        val passwordObserver = mockk<Observer<Boolean>>(relaxed = true)

        initViewModel()
        loginViewModel.isPasswordEmpty.observeForever(passwordObserver)
        loginViewModel.makeLogin(USERNAME, "")

        assertForLoginField(loginViewModel.isPasswordEmpty, passwordObserver, true)
        coVerify(exactly = 0) { loginUseCase.makeLogin(any()) }
    }

    @Test
    fun `checkIfPasswordFieldIsEmpty - Verify that password field is not empty`() {
        val passwordObserver = mockk<Observer<Boolean>>(relaxed = true)

        initViewModel()
        loginViewModel.isPasswordEmpty.observeForever(passwordObserver)
        loginViewModel.makeLogin(USERNAME, PASSWORD)

        assertForLoginField(loginViewModel.isPasswordEmpty, passwordObserver, false)
        coVerify(exactly = 1) { loginUseCase.makeLogin(any()) }
    }

    @Test
    fun `validateFields - Verify that username and password field is empty`() {
        val usernameObserver = mockk<Observer<Boolean>>(relaxed = true)
        val passwordObserver = mockk<Observer<Boolean>>(relaxed = true)

        initViewModel()
        loginViewModel.isUsernameEmpty.observeForever(usernameObserver)
        loginViewModel.isPasswordEmpty.observeForever(passwordObserver)
        loginViewModel.makeLogin("", "")

        assertForLoginField(loginViewModel.isUsernameEmpty, usernameObserver, true)
        assertForLoginField(loginViewModel.isPasswordEmpty, passwordObserver, true)
        coVerify(exactly = 0) { loginUseCase.makeLogin(any()) }
    }

    @Test
    fun `validateFields - Verify that username and password field is not empty`() {
        val usernameObserver = mockk<Observer<Boolean>>(relaxed = true)
        val passwordObserver = mockk<Observer<Boolean>>(relaxed = true)

        initViewModel()
        loginViewModel.isUsernameEmpty.observeForever(usernameObserver)
        loginViewModel.isPasswordEmpty.observeForever(passwordObserver)
        loginViewModel.makeLogin(USERNAME, PASSWORD)

        assertForLoginField(loginViewModel.isUsernameEmpty, usernameObserver, false)
        assertForLoginField(loginViewModel.isPasswordEmpty, passwordObserver, false)
        coVerify(exactly = 1) { loginUseCase.makeLogin(any()) }
    }

    @Test
    fun `login - Verify that login is successful`() {
        val loadingObserver = mockk<Observer<Boolean>>(relaxed = true)
        val loginState = mockk<Observer<Event<Resource<Unit>>>>(relaxed = true)

        initViewModel()
        loginViewModel.showLoading.observeForever(loadingObserver)
        loginViewModel.loginState.observeForever(loginState)
        loginViewModel.makeLogin(USERNAME, PASSWORD)

        verify { loadingObserver.onChanged(true) }
        coVerify { loginUseCase.makeLogin(mockLoginUI()) }
        verify { loginState.onChanged(any()) }
        verify { loadingObserver.onChanged(false) }
        assertThat(loginViewModel.loginState.value?.getContentIfNotHandled(), instanceOf(Resource.Success::class.java))
    }

    @Test
    fun `login - Verify that login is not successful`() {
        val loadingObserver = mockk<Observer<Boolean>>(relaxed = true)
        val loginState = mockk<Observer<Event<Resource<Unit>>>>(relaxed = true)

        coEvery { loginUseCase.makeLogin(mockLoginUI()) } throws Exception()

        initViewModel()
        loginViewModel.showLoading.observeForever(loadingObserver)
        loginViewModel.loginState.observeForever(loginState)
        loginViewModel.makeLogin(USERNAME, PASSWORD)

        verify { loadingObserver.onChanged(true) }
        coVerify { loginUseCase.makeLogin(mockLoginUI()) }
        verify { loginState.onChanged(any()) }
        verify { loadingObserver.onChanged(false) }
        assertThat(loginViewModel.loginState.value?.getContentIfNotHandled()?.exception, instanceOf(Exception::class.java))
    }

    private fun initViewModel() {
        loginViewModel = LoginViewModel(loginUseCase)
    }

    private fun mockLoginUI(): LoginUI {
        return LoginUI(
            username = USERNAME,
            password = PASSWORD
        )
    }

    companion object {
        const val USERNAME = "matheus"
        const val PASSWORD = "123"
    }
}
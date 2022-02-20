package com.glpi.ifsp.hortolandia.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.glpi.ifsp.hortolandia.domain.LoginUseCase
import com.glpi.ifsp.hortolandia.infrastructure.exceptions.UnauthorizedLoginException
import com.glpi.ifsp.hortolandia.ui.event.LoginEvent
import com.glpi.ifsp.hortolandia.ui.model.LoginUI
import com.glpi.ifsp.hortolandia.ui.state.LoginState
import com.hadilq.liveevent.LiveEvent
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private var isPasswordEmpty: Boolean = true
    private var isUsernameEmpty: Boolean = true

    private val _state = MutableLiveData<LoginState>()
    val state: LiveData<LoginState>
        get() = _state

    private var _event = LiveEvent<LoginEvent>()
    val event: LiveData<LoginEvent>
        get() = _event

    fun onLoginClick(username: String, password: String) {
        validateFields(username, password)
        makeLoginIfUsernameAndPasswordAreFilled(username, password)
    }

    private fun makeLoginIfUsernameAndPasswordAreFilled(
        username: String,
        password: String
    ) {
        if (!isUsernameEmpty && !isPasswordEmpty) {
            val loginUI = LoginUI(username, password)
            viewModelScope.launch {
                login(loginUI)
            }
        }
    }

    private suspend fun login(loginUI: LoginUI) {
        try {
            _state.value = LoginState.ShowLoading
            loginUseCase.makeLogin(loginUI)
            _event.value = LoginEvent.OpenHome
        } catch (e: UnauthorizedLoginException) {
            _event.value = LoginEvent.ShowBadCredentialError
        } catch (e: Exception) {
            _event.value = LoginEvent.ShowConnectionError
        } finally {
            _state.value = LoginState.HideLoading
        }
    }

    private fun validateFields(username: String, password: String) {
        checkIfUsernameFieldIsEmpty(username)
        checkIfPasswordFieldIsEmpty(password)
        _state.value = LoginState.ValidateField(isUsernameEmpty, isPasswordEmpty)
    }

    private fun checkIfUsernameFieldIsEmpty(username: String) {
        isUsernameEmpty = username == ""
    }

    private fun checkIfPasswordFieldIsEmpty(password: String) {
        isPasswordEmpty = password == ""
    }
}

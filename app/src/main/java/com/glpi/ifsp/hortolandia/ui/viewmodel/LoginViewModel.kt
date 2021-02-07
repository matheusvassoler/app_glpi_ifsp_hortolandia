package com.glpi.ifsp.hortolandia.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.glpi.ifsp.hortolandia.data.resource.Resource
import com.glpi.ifsp.hortolandia.domain.LoginUseCase
import com.glpi.ifsp.hortolandia.ui.LoginUI
import com.glpi.ifsp.hortolandia.ui.wrapper.Event
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _isUsernameEmpty = MutableLiveData<Boolean>()
    val isUsernameEmpty: LiveData<Boolean>
        get() = _isUsernameEmpty

    private val _isPasswordEmpty = MutableLiveData<Boolean>()
    val isPasswordEmpty: LiveData<Boolean>
        get() = _isPasswordEmpty

    private val _showLoading = MutableLiveData<Boolean>()
    val showLoading: LiveData<Boolean>
        get() = _showLoading

    private val _loginState = MutableLiveData<Event<Resource<Unit>>>()
    val loginState: LiveData<Event<Resource<Unit>>>
        get() = _loginState

    fun makeLogin(username: String, password: String) {
        validateFields(username, password)
        makeLoginIfUsernameAndPasswordAreFilled(username, password)
    }

    private fun makeLoginIfUsernameAndPasswordAreFilled(
        username: String,
        password: String
    ) {
        if (_isUsernameEmpty.value == false && _isPasswordEmpty.value == false) {
            val loginUI = LoginUI(username, password)
            viewModelScope.launch {
                login(loginUI)
            }
        }
    }

    private suspend fun login(loginUI: LoginUI) {
        try {
            _showLoading.value = true
            loginUseCase.makeLogin(loginUI)
            _loginState.value = Event(Resource.Success())
        }  catch (e: Exception) {
            _loginState.value = Event(Resource.Error(e))
        } finally {
            _showLoading.value = false
        }
    }

    private fun validateFields(username: String, password: String) {
        checkIfUsernameFieldIsEmpty(username)
        checkIfPasswordFieldIsEmpty(password)
    }

    private fun checkIfUsernameFieldIsEmpty(username: String) {
        _isUsernameEmpty.value = username == ""
    }

    private fun checkIfPasswordFieldIsEmpty(password: String) {
        _isPasswordEmpty.value = password == ""
    }
}

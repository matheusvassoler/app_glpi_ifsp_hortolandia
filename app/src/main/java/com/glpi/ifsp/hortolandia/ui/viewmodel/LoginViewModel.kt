package com.glpi.ifsp.hortolandia.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.glpi.ifsp.hortolandia.domain.LoginUseCase

class LoginViewModel(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _isUsernameEmpty = MutableLiveData<Boolean>()
    val isUsernameEmpty: LiveData<Boolean>
        get() = _isUsernameEmpty

    private val _isPasswordEmpty = MutableLiveData<Boolean>()
    val isPasswordEmpty: LiveData<Boolean>
        get() = _isPasswordEmpty

    fun validateFields(username: String, password: String) {
        checkIfUsernameFieldIsEmpty(username)
        checkIfPasswordFieldIsEmpty(password)

        if (_isUsernameEmpty.value == false && _isPasswordEmpty.value == false) {
          // TODO - Implementar na task TIF-24
        }
    }

    private fun checkIfUsernameFieldIsEmpty(username: String) {
        _isUsernameEmpty.value = username == ""
    }

    private fun checkIfPasswordFieldIsEmpty(password: String) {
        _isPasswordEmpty.value = password == ""
    }
}

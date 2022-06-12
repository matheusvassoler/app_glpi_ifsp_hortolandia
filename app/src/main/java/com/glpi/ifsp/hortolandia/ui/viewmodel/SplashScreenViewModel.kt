package com.glpi.ifsp.hortolandia.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.glpi.ifsp.hortolandia.domain.IsValidLoginUseCase
import com.glpi.ifsp.hortolandia.ui.event.SplashScreenEvent
import kotlinx.coroutines.launch

class SplashScreenViewModel(
    private val isValidLoginUseCase: IsValidLoginUseCase
) : ViewModel() {

    private var _event = MutableLiveData<SplashScreenEvent>()
    val event: LiveData<SplashScreenEvent>
        get() = _event

    fun checkIfThereIsAValidLogin() {
        viewModelScope.launch {
            try {
                if (isValidLoginUseCase()) {
                    _event.value = SplashScreenEvent.OpenHome
                } else {
                    _event.value = SplashScreenEvent.OpenLogin
                }
            } catch (e: Exception) {
                _event.value = SplashScreenEvent.OpenLogin
            }
        }
    }
}

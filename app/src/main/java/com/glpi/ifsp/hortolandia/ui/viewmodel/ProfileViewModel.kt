package com.glpi.ifsp.hortolandia.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.glpi.ifsp.hortolandia.domain.LogoutUseCase
import com.glpi.ifsp.hortolandia.domain.SessionUseCase
import com.glpi.ifsp.hortolandia.infrastructure.exceptions.InternalErrorException
import com.glpi.ifsp.hortolandia.infrastructure.exceptions.UnauthorizedLoginException
import com.glpi.ifsp.hortolandia.ui.event.LogoutEvent
import com.glpi.ifsp.hortolandia.ui.state.ProfileState
import com.hadilq.liveevent.LiveEvent
import java.lang.Exception
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val logoutUseCase: LogoutUseCase,
    private val sessionUseCase: SessionUseCase
) : ViewModel() {

    private var _state = MutableLiveData<ProfileState>()
    val state: LiveData<ProfileState>
        get() = _state

    private var _event = LiveEvent<LogoutEvent>()
    val event: LiveData<LogoutEvent>
        get() = _event

    fun getPersonalData() {
        sessionUseCase.getPersonalData().run {
            _state.value = ProfileState.ShowPersonalData(
                id = this.id,
                username = this.username,
                firstName = this.firstName,
                lastName = this.lastName
            )
        }
    }

    fun onLogoutClick() {
        viewModelScope.launch() {
            try {
                _state.value = ProfileState.ShowLoading
                logoutUseCase()
                _event.value = LogoutEvent.GoToLogin
            } catch (e: UnauthorizedLoginException) {
                _event.value = LogoutEvent.GoToLogin
            } catch (e: InternalErrorException) {
                _event.value = LogoutEvent.ShowInternalError
            } catch (e: Exception) {
                _event.value = LogoutEvent.ShowLogoutError
            } finally {
                _state.value = ProfileState.HideLoading
            }
        }
    }
}

package com.glpi.ifsp.hortolandia.domain

import com.glpi.ifsp.hortolandia.data.repository.session.SessionRepository
import com.glpi.ifsp.hortolandia.ui.model.SessionUI
import com.glpi.ifsp.hortolandia.ui.model.UserUI

class SessionUseCase(
    private val sessionRepository: SessionRepository
) {

    fun storeSessionData(sessionUI: SessionUI) {
        sessionRepository.storeUserData(sessionUI)
    }

    fun clearSessionData() {
        sessionRepository.clearUserData()
    }

    fun getSessionToken(): String? {
        return sessionRepository.getSessionToken()
    }

    fun getPersonalData(): UserUI {
        return UserUI(
            sessionRepository.getId(),
            sessionRepository.getUsername() ?: "",
            sessionRepository.getFirstName() ?: "",
            sessionRepository.getLastName() ?: ""
        )
    }
}

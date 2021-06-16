package com.glpi.ifsp.hortolandia.domain

import com.glpi.ifsp.hortolandia.data.repository.logout.LogoutRepository
import com.glpi.ifsp.hortolandia.infrastructure.exceptions.ResponseRequestException

class LogoutUseCase(
    private val logoutRepository: LogoutRepository,
    private val sessionUseCase: SessionUseCase
) {

    suspend operator fun invoke(sessionToken: String) {
        val response = logoutRepository.killSession(sessionToken)

        if (response.isSuccessful) {
            sessionUseCase.clearSessionData()
        } else {
            throw ResponseRequestException()
        }
    }
}

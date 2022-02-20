package com.glpi.ifsp.hortolandia.domain

import com.glpi.ifsp.hortolandia.data.repository.logout.LogoutRepository
import com.glpi.ifsp.hortolandia.infrastructure.Constant
import com.glpi.ifsp.hortolandia.infrastructure.exceptions.InternalErrorException
import com.glpi.ifsp.hortolandia.infrastructure.exceptions.ResponseRequestException
import com.glpi.ifsp.hortolandia.infrastructure.exceptions.UnauthorizedLoginException
import retrofit2.Response

class LogoutUseCase(
    private val logoutRepository: LogoutRepository,
    private val sessionUseCase: SessionUseCase
) {

    suspend operator fun invoke() {
        val sessionToken = sessionUseCase.getSessionToken() ?: throw InternalErrorException()

        val response = logoutRepository.killSession(sessionToken)

        clearUserSession(response)
    }

    private fun clearUserSession(response: Response<Void>) {
        if (response.isSuccessful) {
            sessionUseCase.clearSessionData()
        } else if (response.code() == Constant.RequestStatusCode.UNAUTHORIZED ||
            response.code() == Constant.RequestStatusCode.FORBIDDEN
        ) {
            throw UnauthorizedLoginException()
        } else {
            throw ResponseRequestException()
        }
    }
}

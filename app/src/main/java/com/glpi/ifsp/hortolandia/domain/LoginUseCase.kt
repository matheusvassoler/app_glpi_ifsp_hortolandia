package com.glpi.ifsp.hortolandia.domain

import com.glpi.ifsp.hortolandia.data.model.Login
import com.glpi.ifsp.hortolandia.data.repository.login.LoginRepository
import com.glpi.ifsp.hortolandia.infrastructure.Constant
import com.glpi.ifsp.hortolandia.infrastructure.exceptions.ResponseRequestException
import com.glpi.ifsp.hortolandia.infrastructure.exceptions.UnauthorizedLoginException
import com.glpi.ifsp.hortolandia.ui.LoginUI
import com.glpi.ifsp.hortolandia.ui.SessionUI
import com.glpi.ifsp.hortolandia.ui.UserUI
import retrofit2.Response
import java.lang.Exception

class LoginUseCase(
    private val sessionUseCase: SessionUseCase,
    private val loginRepository: LoginRepository
) {

    suspend fun makeLogin(loginUI: LoginUI) {
        requestLogin(loginUI)
    }

    private suspend fun requestLogin(loginUI: LoginUI) {
        try {
            getLoginResponse(loginUI)
        } catch (e: Exception) {
            throw e
        }
    }

    private suspend fun getLoginResponse(loginUI: LoginUI) {
        val loginResponse = loginRepository.makeLogin(loginUI)
        checkIfLoginIsSuccessful(loginResponse)
    }

    private fun checkIfLoginIsSuccessful(loginResponse: Response<Login>) {
        when {
            loginResponse.isSuccessful -> {
                checkIfResponseBodyHasValue(loginResponse)
            }
            loginResponse.code() == Constant.RequestStatusCode.UNAUTHORIZED -> {
                throw UnauthorizedLoginException()
            }
            else -> {
                throw ResponseRequestException()
            }
        }
    }

    private fun checkIfResponseBodyHasValue(loginResponse: Response<Login>) {
        val loginData = loginResponse.body()
        if (loginData != null) {
            val sessionUI = convertResponseBodyToSessionUI(loginData)
            storeSessionDataLocally(sessionUI)
        }
    }

    private fun convertResponseBodyToSessionUI(loginData: Login): SessionUI {
        val user = loginData.user
        return SessionUI(
            sessionToken = loginData.sessionToken,
            userUI = UserUI(
                id = user.id.toInt(),
                username = user.username,
                firstName = user.firstName,
                lastName = user.lastName
            )
        )
    }

    private fun storeSessionDataLocally(sessionUI: SessionUI) =
        sessionUseCase.storeSessionData(sessionUI)
}

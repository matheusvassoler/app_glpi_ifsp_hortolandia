package com.glpi.ifsp.hortolandia.domain

import android.util.Log
import com.glpi.ifsp.hortolandia.data.model.Login
import com.glpi.ifsp.hortolandia.data.model.Session
import com.glpi.ifsp.hortolandia.data.model.User
import com.glpi.ifsp.hortolandia.data.repository.login.LoginRepository
import com.glpi.ifsp.hortolandia.infrastructure.Constant
import com.glpi.ifsp.hortolandia.infrastructure.exceptions.ResponseRequestException
import com.glpi.ifsp.hortolandia.infrastructure.exceptions.UnauthorizedLoginException
import com.glpi.ifsp.hortolandia.ui.model.LoginUI
import com.glpi.ifsp.hortolandia.ui.model.SessionUI
import com.glpi.ifsp.hortolandia.ui.model.UserUI
import java.lang.Exception
import retrofit2.Response

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

    private suspend fun checkIfLoginIsSuccessful(loginResponse: Response<Login>) {
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

    private suspend fun checkIfResponseBodyHasValue(loginResponse: Response<Login>) {
        val loginData = loginResponse.body()
        if (loginData != null) {
            val sessionUI = convertResponseBodyToSessionUI(loginData)
            storeSessionDataLocally(sessionUI)
        }
    }

    private suspend fun convertResponseBodyToSessionUI(loginData: Login): SessionUI {
        val user: User = loginData.user ?: getUserInfo(loginData)
        // TODO - Apagar isso no futuro, apenas para log
        Log.i("APP_TOKEN_SESSION", loginData.sessionToken)
        return SessionUI(
            sessionToken = loginData.sessionToken,
            userUI = UserUI(
                id = user.id.toInt(),
                username = user.username ?: "",
                firstName = user.firstName ?: "",
                lastName = user.lastName ?: ""
            )
        )
    }

    private suspend fun getUserInfo(loginData: Login): User {
        val response = loginRepository.getUserInfo(loginData.sessionToken)
        if (response.isSuccessful) {
            val session: Session? = response.body()
            if (session != null) {
                return session.user
            } else {
                throw ResponseRequestException()
            }
        } else {
            throw ResponseRequestException()
        }
    }

    private fun storeSessionDataLocally(sessionUI: SessionUI) =
        sessionUseCase.storeSessionData(sessionUI)
}

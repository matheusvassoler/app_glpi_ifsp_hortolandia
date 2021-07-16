package com.glpi.ifsp.hortolandia.data.repository.login

import android.util.Base64
import android.util.Base64.encodeToString
import com.glpi.ifsp.hortolandia.data.model.Login
import com.glpi.ifsp.hortolandia.data.source.remote.ApiClient
import com.glpi.ifsp.hortolandia.ui.model.LoginUI
import retrofit2.Response

class LoginRemoteRepository(
    private val apiClient: ApiClient
) : LoginRepository {

    override suspend fun makeLogin(login: LoginUI): Response<Login> {
        val usernameAndPassword = prepareUsernameAndPasswordForBase64(login)
        val authHeader = createBase64StringForAuthHeader(usernameAndPassword)
        return apiClient().callLoginResponse(authHeader)
    }

    private fun createBase64StringForAuthHeader(base: String): String =
        "Basic " + encodeToString(base.toByteArray(), Base64.NO_WRAP)

    private fun prepareUsernameAndPasswordForBase64(login: LoginUI): String =
        login.username + ":" + login.password
}

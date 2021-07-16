package com.glpi.ifsp.hortolandia.data.repository.login

import com.glpi.ifsp.hortolandia.data.model.Login
import com.glpi.ifsp.hortolandia.ui.model.LoginUI
import retrofit2.Response

interface LoginRepository {
    suspend fun makeLogin(login: LoginUI): Response<Login>
}

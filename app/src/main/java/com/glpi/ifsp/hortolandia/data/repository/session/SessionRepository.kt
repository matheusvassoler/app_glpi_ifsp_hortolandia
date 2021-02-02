package com.glpi.ifsp.hortolandia.data.repository.session

import com.glpi.ifsp.hortolandia.data.model.Login
import com.glpi.ifsp.hortolandia.ui.LoginUI
import retrofit2.Response

interface SessionRepository {
    suspend fun makeLogin(login: LoginUI): Response<Login>
}

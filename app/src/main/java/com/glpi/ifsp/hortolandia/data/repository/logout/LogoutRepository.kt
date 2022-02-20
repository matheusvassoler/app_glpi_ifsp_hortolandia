package com.glpi.ifsp.hortolandia.data.repository.logout

import retrofit2.Response

interface LogoutRepository {
    suspend fun killSession(sessionToken: String): Response<Void>
}

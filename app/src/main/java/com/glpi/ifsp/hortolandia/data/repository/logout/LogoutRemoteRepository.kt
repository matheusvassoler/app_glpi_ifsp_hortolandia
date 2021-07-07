package com.glpi.ifsp.hortolandia.data.repository.logout

import com.glpi.ifsp.hortolandia.data.source.remote.ApiClient
import retrofit2.Response

class LogoutRemoteRepository(
    private val apiClient: ApiClient
) : LogoutRepository {

    override suspend fun killSession(sessionToken: String): Response<Void> {
        return apiClient().killSession(sessionToken)
    }
}

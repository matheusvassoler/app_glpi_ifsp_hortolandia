package com.glpi.ifsp.hortolandia.data.repository.profile

import com.glpi.ifsp.hortolandia.data.source.remote.ApiClient
import retrofit2.Response

class ProfileRemoteRepository(private val apiClient: ApiClient) : ProfileRepository {

    override suspend fun checkIfLoginOfLoggedProfileIsStillValid(
        sessionToken: String
    ): Response<Void> {
        return apiClient().getActiveProfile(sessionToken)
    }
}

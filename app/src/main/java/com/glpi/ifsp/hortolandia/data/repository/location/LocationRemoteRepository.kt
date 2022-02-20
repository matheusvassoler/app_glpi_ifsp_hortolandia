package com.glpi.ifsp.hortolandia.data.repository.location

import com.glpi.ifsp.hortolandia.data.model.Location
import com.glpi.ifsp.hortolandia.data.source.remote.ApiClient
import retrofit2.Response

class LocationRemoteRepository(private val api: ApiClient) : LocationRepository {

    override suspend fun getLocations(sessionToken: String): Response<List<Location>> {
        return api().getLocations(sessionToken)
    }
}

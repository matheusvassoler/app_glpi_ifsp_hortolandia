package com.glpi.ifsp.hortolandia.data.repository.location

import com.glpi.ifsp.hortolandia.data.model.Location
import retrofit2.Response

interface LocationRepository {

    suspend fun getLocations(sessionToken: String): Response<List<Location>>
}

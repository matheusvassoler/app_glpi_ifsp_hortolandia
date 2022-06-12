package com.glpi.ifsp.hortolandia.data.repository.profile

import retrofit2.Response

interface ProfileRepository {

    suspend fun checkIfLoginOfLoggedProfileIsStillValid(sessionToken: String): Response<Void>
}

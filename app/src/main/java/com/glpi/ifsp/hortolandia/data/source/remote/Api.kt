package com.glpi.ifsp.hortolandia.data.source.remote

import com.glpi.ifsp.hortolandia.data.model.Login
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query

private const val APP_TOKEN = "teFbYFpVnJ3XLXgzDMtqe3RLvnru34dEN9oyDkk1"

interface Api {

    @GET("initSession")
    @Headers("App-Token: $APP_TOKEN")
    suspend fun callLoginResponse(
        @Header("Authorization") authHeader: String,
        @Query("get_full_session") getFullSession: Boolean = true
    ): Response<Login>
}

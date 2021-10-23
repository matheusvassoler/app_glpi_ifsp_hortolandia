package com.glpi.ifsp.hortolandia.data.source.remote

import com.glpi.ifsp.hortolandia.data.model.Login
import com.glpi.ifsp.hortolandia.data.model.Ticket
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query

private const val APP_TOKEN = "2lCh2VLJ7ERtvmoH5mlFsJTGCStGFfI1r6cUEt23"

interface Api {

    @GET("initSession")
    @Headers("App-Token: $APP_TOKEN")
    suspend fun callLoginResponse(
        @Header("Authorization") authHeader: String,
        @Query("get_full_session") getFullSession: Boolean = true
    ): Response<Login>

    @GET("killSession")
    @Headers("App-Token: $APP_TOKEN")
    suspend fun killSession(
        @Header("Session-Token") sessionToken: String
    ): Response<Void>

    @GET("ticket")
    @Headers("App-Token: $APP_TOKEN")
    suspend fun getTickets(
        @Header("Session-Token") sessionToken: String,
        @Query("range") searchRange: String
    ): Response<List<Ticket>>
}

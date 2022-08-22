package com.glpi.ifsp.hortolandia.data.source.remote

import com.glpi.ifsp.hortolandia.data.model.Form
import com.glpi.ifsp.hortolandia.data.model.Item
import com.glpi.ifsp.hortolandia.data.model.Location
import com.glpi.ifsp.hortolandia.data.model.Login
import com.glpi.ifsp.hortolandia.data.model.Session
import com.glpi.ifsp.hortolandia.data.model.Ticket
import com.glpi.ifsp.hortolandia.data.model.TicketInput
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

private const val APP_TOKEN = "aT5zDoqXEFUE2QM7ko9RAPcsZtGlqTNn3A91xSQW"

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

    @GET("PluginFormcreatorForm/{form_id}")
    @Headers("App-Token: $APP_TOKEN")
    suspend fun getForm(
        @Header("Session-Token") sessionToken: String,
        @Path(value = "form_id") formId: Int
    ): Response<Form>

    @GET("{item_type}")
    @Headers("App-Token: $APP_TOKEN")
    suspend fun getItems(
        @Header("Session-Token") sessionToken: String,
        @Path(value = "item_type") itemType: String
    ): Response<List<Item>>

    @GET("Location")
    @Headers("App-Token: $APP_TOKEN")
    suspend fun getLocations(
        @Header("Session-Token") sessionToken: String
    ): Response<List<Location>>

    @GET("getFullSession")
    @Headers("App-Token: $APP_TOKEN")
    suspend fun getUserInfo(
        @Header("Session-Token") sessionToken: String
    ): Response<Session>

    @POST("Ticket")
    @Headers("App-Token: $APP_TOKEN")
    suspend fun createTicket(
        @Header("Session-Token") sessionToken: String,
        @Body ticketInput: TicketInput
    ): Response<Void>

    @GET("getActiveProfile")
    @Headers("App-Token: $APP_TOKEN")
    suspend fun getActiveProfile(
        @Header("Session-Token") sessionToken: String
    ): Response<Void>
}

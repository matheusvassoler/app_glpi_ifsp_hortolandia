package com.glpi.ifsp.hortolandia.data.source.remote

import com.glpi.ifsp.hortolandia.data.model.Login
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers

private const val APP_TOKEN = "Jlfcuigy4rfhdLuK2NwLsaNF9RaXv2QTctTDOFnE"

interface Api {

    @GET("initSession")
    @Headers("App-Token: $APP_TOKEN")
    fun callLoginResponse(
        @Header("Authorization") authHeader: String,
    ): Call<Login>
}

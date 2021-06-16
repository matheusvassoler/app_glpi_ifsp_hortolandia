package com.glpi.ifsp.hortolandia.data.model

import com.google.gson.annotations.SerializedName

data class Login(
    @SerializedName("session_token") val sessionToken: String,
    @SerializedName("session") val user: User
)

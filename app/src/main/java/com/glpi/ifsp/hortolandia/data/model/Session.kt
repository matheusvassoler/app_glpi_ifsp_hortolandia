package com.glpi.ifsp.hortolandia.data.model

import com.google.gson.annotations.SerializedName

data class Session(
    @SerializedName("session") val user: User
)

package com.glpi.ifsp.hortolandia.data.model

import com.google.gson.annotations.SerializedName

data class Ticket(
    @SerializedName("name") val name: String,
    @SerializedName("content") val content: String,
    @SerializedName("date_mod") val updateDate: String,
    @SerializedName("status") val status: Int
)

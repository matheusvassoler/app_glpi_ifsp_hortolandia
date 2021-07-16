package com.glpi.ifsp.hortolandia.data.model

import com.google.gson.annotations.SerializedName

data class Ticket(
    @SerializedName("id") val id: String,
    @SerializedName("name") val title: String,
    @SerializedName("content") val content: String,
    @SerializedName("date_mod") val updateDate: String,
    @SerializedName("status") val status: Int,
    @SerializedName("date") val openingDate: String
)

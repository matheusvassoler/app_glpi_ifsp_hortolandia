package com.glpi.ifsp.hortolandia.data.model

import com.google.gson.annotations.SerializedName

data class TicketData(
    @SerializedName("name") val name: String,
    @SerializedName("content") val content: String,
    @SerializedName("status") val status: Int,
    @SerializedName("urgency") val urgency: Int
)

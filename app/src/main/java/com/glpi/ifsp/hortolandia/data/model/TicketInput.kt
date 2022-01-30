package com.glpi.ifsp.hortolandia.data.model

import com.google.gson.annotations.SerializedName

data class TicketInput(
    @SerializedName("input") val ticketData: TicketData
)

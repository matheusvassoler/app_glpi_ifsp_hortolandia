package com.glpi.ifsp.hortolandia.data.model

import com.google.gson.annotations.SerializedName

data class Item(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String
)

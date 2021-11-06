package com.glpi.ifsp.hortolandia.data.model

import com.google.gson.annotations.SerializedName

data class Location(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("locations_id") val parentLocation: Int,
    @SerializedName("level") val hierarchicalLevelOfLocation: Int,
    @SerializedName("sons_cache") val subLocationsId: String,
    @SerializedName("completename") val completeName: String
)

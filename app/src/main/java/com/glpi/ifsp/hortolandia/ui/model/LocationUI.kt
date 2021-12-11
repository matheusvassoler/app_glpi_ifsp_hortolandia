package com.glpi.ifsp.hortolandia.ui.model

data class LocationUI(
    val id: Int,
    val name: String,
    val parentLocation: Int,
    val hierarchicalLevelOfLocation: Int,
    val subLocationsId: List<Int>?,
    val completeName: String
)

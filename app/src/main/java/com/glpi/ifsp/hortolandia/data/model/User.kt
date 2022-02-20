package com.glpi.ifsp.hortolandia.data.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("glpiID") val id: String,
    @SerializedName("glpifirstname") val firstName: String,
    @SerializedName("glpirealname") val lastName: String,
    @SerializedName("glpiname") val username: String
)

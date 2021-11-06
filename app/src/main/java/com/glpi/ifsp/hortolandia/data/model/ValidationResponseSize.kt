package com.glpi.ifsp.hortolandia.data.model

import com.google.gson.annotations.SerializedName

data class ValidationResponseSize(
    @SerializedName("id") val id: Int,
    @SerializedName("plugin_formcreator_questions_id") val questionIdThatHasValidation: Int,
    @SerializedName("range_min") val minimumNumberOfCharacters: String,
    @SerializedName("range_max") val maximumNumberOfCharacters: String
)

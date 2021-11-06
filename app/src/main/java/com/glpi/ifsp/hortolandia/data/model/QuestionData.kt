package com.glpi.ifsp.hortolandia.data.model

import com.google.gson.annotations.SerializedName

data class QuestionData(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("fieldtype") val fieldType: String,
    @SerializedName("values") val values: String?,
    @SerializedName("show_rule") val showRule: Int,
    @SerializedName("order") val order: Int,
    @SerializedName("description") val description: String,
    @SerializedName("required") val required: Int
)

package com.glpi.ifsp.hortolandia.data.model

import com.google.gson.annotations.SerializedName

data class RuleToShowQuestion(
    @SerializedName("id") val id: Int,
    @SerializedName("items_id") val itemsId: Int,
    @SerializedName("plugin_formcreator_questions_id") val pluginFormCreatorQuestionsId: Int,
    @SerializedName("show_value") val showValue: String,
    @SerializedName("show_condition") val showCondition: Int
)

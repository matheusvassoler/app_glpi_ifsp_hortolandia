package com.glpi.ifsp.hortolandia.data.model

import com.google.gson.annotations.SerializedName

data class RuleToShowQuestion(
    @SerializedName("id") val id: Int,
    @SerializedName("items_id") val questionIdThatDisappearsOrAppearsBasedOnACondition: Int,
    @SerializedName("plugin_formcreator_questions_id") val questionIdThatControlsTheCondition: Int,
    @SerializedName("show_value") val valueThatTriggersCondition: String,
    @SerializedName("show_condition") val comparisonMethod: Int
)

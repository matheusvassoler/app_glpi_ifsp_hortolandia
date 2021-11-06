package com.glpi.ifsp.hortolandia.data.model

import com.google.gson.annotations.SerializedName

data class FormData(
    @SerializedName("name") val name: String,
    @SerializedName("content") val contentDescription: String,
    @SerializedName("_questions") val questions: Map<String, QuestionData>,
    @SerializedName("_conditions") val conditionsToHideOrShowQuestions: Map<String, RuleToShowQuestion>,
    @SerializedName("_ranges") val validationOfCharacterSizesInFields: Map<String, ValidationResponseSize>
)

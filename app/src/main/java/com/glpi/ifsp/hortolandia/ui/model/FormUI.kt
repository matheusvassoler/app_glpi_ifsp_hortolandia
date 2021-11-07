package com.glpi.ifsp.hortolandia.ui.model

data class FormUI(
    val name: String,
    val contentDescription: String,
    val questions: List<QuestionUI>,
    val conditionsToHideOrShowQuestions: List<RuleToShowQuestionUI>,
    val validationOfCharacterSizesInFields: List<ValidationResponseSizeUI>
)

package com.glpi.ifsp.hortolandia.ui.model

data class ValidationResponseSizeUI(
    val id: Int,
    val questionIdThatHasValidation: Int,
    val minimumNumberOfCharacters: Int?,
    val maximumNumberOfCharacters: Int?
)

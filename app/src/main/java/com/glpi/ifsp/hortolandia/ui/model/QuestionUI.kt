package com.glpi.ifsp.hortolandia.ui.model

import com.glpi.ifsp.hortolandia.data.enums.FieldRule
import com.glpi.ifsp.hortolandia.data.enums.FieldType

data class QuestionUI(
    val id: Int,
    val name: String,
    val description: String,
    val fieldType: FieldType,
    val values: String?,
    val fieldRule: FieldRule,
    val displayOrder: Int,
    val isRequired: Boolean
)

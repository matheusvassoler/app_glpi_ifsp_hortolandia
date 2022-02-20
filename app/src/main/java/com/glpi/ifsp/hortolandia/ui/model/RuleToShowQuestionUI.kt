package com.glpi.ifsp.hortolandia.ui.model

import com.glpi.ifsp.hortolandia.data.enums.ComparisonMethod

data class RuleToShowQuestionUI(
    val id: Int,
    val questionIdThatDisappearsOrAppearsBasedOnACondition: Int,
    val questionIdThatControlsTheCondition: Int,
    val valueThatTriggersCondition: String,
    val comparisonMethod: ComparisonMethod
)

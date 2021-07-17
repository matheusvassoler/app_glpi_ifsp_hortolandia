package com.glpi.ifsp.hortolandia.gateway

interface LabelGateway {

    fun getLabelForStatusNew(): String

    fun getLabelForStatusProcessing(): String

    fun getLabelForStatusPending(): String

    fun getLabelForStatusSolved(): String

    fun getLabelForStatusClosed(): String
}

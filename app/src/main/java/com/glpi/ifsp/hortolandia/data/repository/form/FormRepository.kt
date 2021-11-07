package com.glpi.ifsp.hortolandia.data.repository.form

import com.glpi.ifsp.hortolandia.data.model.Form

interface FormRepository {
    suspend fun getForm(sessionToken: String, formId: Int): Form
}

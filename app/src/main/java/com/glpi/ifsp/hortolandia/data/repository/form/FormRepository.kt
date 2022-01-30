package com.glpi.ifsp.hortolandia.data.repository.form

import com.glpi.ifsp.hortolandia.data.model.Form
import retrofit2.Response

interface FormRepository {
    suspend fun getForm(sessionToken: String, formId: Int): Response<Form>
}

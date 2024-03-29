package com.glpi.ifsp.hortolandia.data.repository.form

import com.glpi.ifsp.hortolandia.data.model.Form
import com.glpi.ifsp.hortolandia.data.source.remote.ApiClient
import retrofit2.Response

class FormRemoteRepository(private val api: ApiClient) : FormRepository {

    override suspend fun getForm(sessionToken: String, formId: Int): Response<Form> {
        return api().getForm(sessionToken, formId)
    }
}

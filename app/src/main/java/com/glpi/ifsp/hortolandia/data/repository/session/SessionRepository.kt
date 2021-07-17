package com.glpi.ifsp.hortolandia.data.repository.session

import com.glpi.ifsp.hortolandia.ui.model.SessionUI

interface SessionRepository {

    fun storeUserData(sessionUI: SessionUI)

    fun clearUserData()

    fun getSessionToken(): String?
}

package com.glpi.ifsp.hortolandia.data.repository.sessiontoken

import com.glpi.ifsp.hortolandia.ui.SessionUI

interface SessionTokenRepository {

    fun storeUserData(sessionUI: SessionUI)
}

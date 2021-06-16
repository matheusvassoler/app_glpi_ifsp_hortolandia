package com.glpi.ifsp.hortolandia.data.repository.session

import com.glpi.ifsp.hortolandia.data.source.local.AppSharedPreferences
import com.glpi.ifsp.hortolandia.ui.SessionUI

class SessionLocalRepository(
    private val appSharedPreferences: AppSharedPreferences
) : SessionRepository {

    override fun storeUserData(sessionUI: SessionUI) {
        storeSessionToken(sessionUI.sessionToken)
        storeUserId(sessionUI.userUI.id)
        storeUsername(sessionUI.userUI.username)
        storeFirstName(sessionUI.userUI.firstName)
        storeLastName(sessionUI.userUI.lastName)
    }

    private fun storeLastName(lastName: String) {
        appSharedPreferences.save("last_name", lastName)
    }

    private fun storeFirstName(firstName: String) {
        appSharedPreferences.save("first_name", firstName)
    }

    private fun storeUsername(username: String) {
        appSharedPreferences.save("username", username)
    }

    private fun storeUserId(id: Int) {
        appSharedPreferences.save("id", id)
    }

    private fun storeSessionToken(sessionToken: String) =
        appSharedPreferences.save("session_token", sessionToken)
}

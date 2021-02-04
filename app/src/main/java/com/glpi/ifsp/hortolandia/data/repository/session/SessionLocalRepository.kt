package com.glpi.ifsp.hortolandia.data.repository.session

import com.glpi.ifsp.hortolandia.data.source.local.AppSharedPreferencesImpl
import com.glpi.ifsp.hortolandia.ui.SessionUI

class SessionLocalRepository(
    private val appSharedPreferencesImpl: AppSharedPreferencesImpl
) : SessionRepository {

    override fun storeUserData(sessionUI: SessionUI) {
        storeSessionToken(sessionUI.sessionToken)
        storeUserId(sessionUI.userUI.id)
        storeUsername(sessionUI.userUI.username)
        storeFirstName(sessionUI.userUI.firstName)
        storeLastName(sessionUI.userUI.lastName)
    }

    private fun storeLastName(lastName: String) {
        appSharedPreferencesImpl.save("last_name", lastName)
    }

    private fun storeFirstName(firstName: String) {
        appSharedPreferencesImpl.save("first_name", firstName)
    }

    private fun storeUsername(username: String) {
        appSharedPreferencesImpl.save("username", username)
    }

    private fun storeUserId(id: Int) {
        appSharedPreferencesImpl.save("id", id)
    }

    private fun storeSessionToken(sessionToken: String) =
        appSharedPreferencesImpl.save("session_token", sessionToken)
}

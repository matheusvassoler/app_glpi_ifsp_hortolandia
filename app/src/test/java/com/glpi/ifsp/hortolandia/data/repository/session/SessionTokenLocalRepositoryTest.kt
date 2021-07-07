package com.glpi.ifsp.hortolandia.data.repository.session

import com.glpi.ifsp.hortolandia.BaseUnitTest
import com.glpi.ifsp.hortolandia.data.source.local.AppSharedPreferencesImpl
import com.glpi.ifsp.hortolandia.ui.SessionUI
import com.glpi.ifsp.hortolandia.ui.UserUI
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.junit.Test

class SessionTokenLocalRepositoryTest : BaseUnitTest() {

    @MockK
    private lateinit var appSharedPreferencesImpl: AppSharedPreferencesImpl

    @InjectMockKs
    private lateinit var sessionLocalRepository: SessionLocalRepository

    @Test
    fun `storeUserData - Check connection with SharedPreferences`() {
        sessionLocalRepository.storeUserData(mockSessionUI())

        verifyCallToSharedPreferences("session_token", "abcdefghij")
        verifyCallToSharedPreferences("id", 1)
        verifyCallToSharedPreferences("username", "matheus")
        verifyCallToSharedPreferences("first_name", "Matheus")
        verifyCallToSharedPreferences("last_name", "Vassoler")
    }

    @Test
    fun `clear method SHOULD call clear to remove data inside SharedPreferences`() {
        sessionLocalRepository.clearUserData()

        verify { appSharedPreferencesImpl.clear() }
    }

    private fun <T> verifyCallToSharedPreferences(keyName: String, value: T) {
        when (value) {
            is Int -> verify { appSharedPreferencesImpl.save(keyName, value) }
            else -> verify { appSharedPreferencesImpl.save(keyName, value as String) }
        }
    }

    private fun mockSessionUI(): SessionUI {
        return SessionUI(
            sessionToken = "abcdefghij",
            userUI = mockUserUI()
        )
    }

    private fun mockUserUI(): UserUI {
        return UserUI(
            id = 1,
            username = "matheus",
            firstName = "Matheus",
            lastName = "Vassoler"
        )
    }
}

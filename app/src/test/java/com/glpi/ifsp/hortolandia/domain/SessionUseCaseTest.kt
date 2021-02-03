package com.glpi.ifsp.hortolandia.domain

import com.glpi.ifsp.hortolandia.BaseUnitTest
import com.glpi.ifsp.hortolandia.data.repository.session.SessionRepository
import com.glpi.ifsp.hortolandia.ui.SessionUI
import com.glpi.ifsp.hortolandia.ui.UserUI
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.junit.Test

class SessionUseCaseTest : BaseUnitTest() {

    @MockK
    private lateinit var sessionRepository: SessionRepository

    @InjectMockKs
    private lateinit var sessionUseCase: SessionUseCase

    @Test
    fun `storeSessionData - Check call to session local repository`() {
        sessionUseCase.storeSessionData(mockSessionUI())

        verify { sessionRepository.storeUserData(mockSessionUI()) }
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
            fistName = "Matheus",
            lastName = "Vassoler"
        )
    }
}

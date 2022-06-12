package com.glpi.ifsp.hortolandia.domain

import com.glpi.ifsp.hortolandia.data.repository.profile.ProfileRepository

class IsValidLoginUseCase(
    private val profileRepository: ProfileRepository,
    private val sessionUseCase: SessionUseCase
) {

    suspend operator fun invoke(): Boolean {
        val sessionToken = sessionUseCase.getSessionToken() ?: ""

        val response = profileRepository.checkIfLoginOfLoggedProfileIsStillValid(sessionToken)

        return response.isSuccessful
    }
}

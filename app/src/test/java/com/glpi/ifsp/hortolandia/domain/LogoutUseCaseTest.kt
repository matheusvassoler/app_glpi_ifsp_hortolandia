package com.glpi.ifsp.hortolandia.domain

import com.glpi.ifsp.hortolandia.BaseUnitTest
import com.glpi.ifsp.hortolandia.data.repository.logout.LogoutRepository
import com.glpi.ifsp.hortolandia.infrastructure.exceptions.InternalErrorException
import com.glpi.ifsp.hortolandia.infrastructure.exceptions.ResponseRequestException
import io.mockk.coEvery
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Test
import retrofit2.Response

class LogoutUseCaseTest : BaseUnitTest() {

    @RelaxedMockK
    private lateinit var logoutRepository: LogoutRepository

    @RelaxedMockK
    private lateinit var sessionUseCase: SessionUseCase

    @InjectMockKs
    private lateinit var logoutUseCase: LogoutUseCase

    @Test
    fun `UseCase SHOULD call clearSessionData of SessionUseCase WHEN logout is successfully`() = runBlocking {
        // GIVEN
        coEvery { sessionUseCase.getSessionToken() } returns "12345"
        coEvery { logoutRepository.killSession("12345") } returns Response.success(null)

        // WHEN
        logoutUseCase()

        // THEN
        verify { sessionUseCase.clearSessionData() }
    }

    @Test(expected = ResponseRequestException::class)
    fun `UseCase SHOULD throw ResponseRequestException WHEN logout was not successful`() = runBlocking {
        // GIVEN
        coEvery { sessionUseCase.getSessionToken() } returns "12345"
        coEvery { logoutRepository.killSession("12345") } returns Response.error(400, "".toResponseBody())

        // WHEN
        logoutUseCase()
    }

    @Test(expected = InternalErrorException::class)
    fun `UseCase SHOULD throw InternalErrorException WHEN session token is null`() = runBlocking {
        // GIVEN
        coEvery { sessionUseCase.getSessionToken() } returns null

        // WHEN
        logoutUseCase()
    }
}

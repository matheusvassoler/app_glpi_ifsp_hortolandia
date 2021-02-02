package com.glpi.ifsp.hortolandia.data.repository.session

import com.glpi.ifsp.hortolandia.BaseUnitTest
import com.glpi.ifsp.hortolandia.data.model.Login
import com.glpi.ifsp.hortolandia.data.model.User
import com.glpi.ifsp.hortolandia.data.source.remote.ApiClient
import com.glpi.ifsp.hortolandia.ui.LoginUI
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.Test
import retrofit2.Response

class SessionRemoteRepositoryTest : BaseUnitTest() {

    @MockK
    private lateinit var apiClient: ApiClient

    @InjectMockKs
    private lateinit var sessionRemoteRepository: SessionRemoteRepository

    @Test
    fun `makeLogin - Check Api call`() = runBlocking {
        coEvery { apiClient().callLoginResponse(any()) } returns mockResponse()

        sessionRemoteRepository.makeLogin(mockLoginUI())

        coVerify { apiClient().callLoginResponse(any()) }
    }

    private fun mockResponse(): Response<Login> {
        val login = Login(
            sessionToken = "1234",
            user = User("1", "Matheus", "Vassoler", "matheus")
        )
        return Response.success(login)
    }

    private fun mockLoginUI(): LoginUI =
        LoginUI("usuario", "senha")
}

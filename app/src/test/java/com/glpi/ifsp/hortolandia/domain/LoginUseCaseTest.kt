package com.glpi.ifsp.hortolandia.domain

import com.glpi.ifsp.hortolandia.BaseUnitTest
import com.glpi.ifsp.hortolandia.data.model.Login
import com.glpi.ifsp.hortolandia.data.model.User
import com.glpi.ifsp.hortolandia.data.repository.login.LoginRepository
import com.glpi.ifsp.hortolandia.infrastructure.exceptions.ResponseRequestException
import com.glpi.ifsp.hortolandia.infrastructure.exceptions.UnauthorizedLoginException
import com.glpi.ifsp.hortolandia.ui.LoginUI
import com.glpi.ifsp.hortolandia.ui.SessionUI
import com.glpi.ifsp.hortolandia.ui.UserUI
import io.mockk.coEvery
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Test
import retrofit2.Response

class LoginUseCaseTest : BaseUnitTest() {

    @MockK
    private lateinit var sessionUseCase: SessionUseCase

    @MockK
    private lateinit var loginRepository: LoginRepository

    @InjectMockKs
    private lateinit var loginUseCase: LoginUseCase

    @Test
    fun `makeLogin - Check if userData is stored locally when login is succesful`() = runBlocking {
        coEvery { loginRepository.makeLogin(any()) } returns mockResponseSuccess()

        loginUseCase.makeLogin(mockLoginUI())

        verify { sessionUseCase.storeSessionData(mockSessionUI()) }
    }

    @Test(expected = Exception::class)
    fun `makeLogin - Check if Exception is throw when there is problem with the API connection`() = runBlocking {
        coEvery { loginRepository.makeLogin(any()) } throws Exception()

        loginUseCase.makeLogin(mockLoginUI())
    }

    @Test(expected = ResponseRequestException::class)
    fun `makeLogin - Check if ResponseRequestException is throw when login is invalid and not is code 401`() = runBlocking {
        coEvery { loginRepository.makeLogin(any()) } returns mockResponseError(500)

        loginUseCase.makeLogin(mockLoginUI())
    }

    @Test(expected = UnauthorizedLoginException::class)
    fun `makeLogin - Check if UnauthorizedLoginException is throw when login is invalid`() = runBlocking {
        coEvery { loginRepository.makeLogin(any()) } returns mockResponseError(401)

        loginUseCase.makeLogin(mockLoginUI())
    }

    private fun mockSessionUI(): SessionUI {
        return SessionUI(
            sessionToken = SESSION_TOKEN,
            userUI = UserUI(
                id = ID.toInt(),
                firstName = FIRST_NAME,
                lastName = LAST_NAME,
                username = USERNAME
            )
        )
    }

    private fun mockResponseSuccess(): Response<Login> {
        val login = Login(
            sessionToken = SESSION_TOKEN,
            user = User(ID, FIRST_NAME, LAST_NAME, USERNAME)
        )
        return Response.success(login)
    }

    private fun mockResponseError(statusCode: Int): Response<Login> =
        Response.error(statusCode, "".toResponseBody())

    private fun mockLoginUI(): LoginUI =
        LoginUI("usuario", "senha")

    companion object {
        const val SESSION_TOKEN = "1234"
        const val ID = "1"
        const val USERNAME = "matheus"
        const val FIRST_NAME = "Matheus"
        const val LAST_NAME = "Vassoler"
    }
}

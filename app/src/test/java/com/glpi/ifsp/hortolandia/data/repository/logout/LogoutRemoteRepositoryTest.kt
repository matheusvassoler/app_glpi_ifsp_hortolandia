package com.glpi.ifsp.hortolandia.data.repository.logout

import com.glpi.ifsp.hortolandia.BaseUnitTest
import com.glpi.ifsp.hortolandia.data.source.remote.ApiClient
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.runBlocking
import org.junit.Test
import retrofit2.Response

class LogoutRemoteRepositoryTest : BaseUnitTest() {

    @RelaxedMockK
    private lateinit var apiClient: ApiClient

    @InjectMockKs
    private lateinit var repository: LogoutRemoteRepository

    @Test
    fun `killSession SHOULD call API to disconnect the user`() = runBlocking {
        // GIVEN
        coEvery { apiClient().killSession("12345") } returns mockResponse()

        // WHEN
        val result = repository.killSession("12345")

        // THEN
        coVerify { apiClient().killSession("12345") }
        result.run {
            assertThat(body()).isNull()
            assertThat(isSuccessful).isTrue()
        }
    }

    private fun mockResponse(): Response<Void> =
        Response.success(null)
}

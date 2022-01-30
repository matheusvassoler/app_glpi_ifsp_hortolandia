package com.glpi.ifsp.hortolandia.data.repository.form

import com.glpi.ifsp.hortolandia.BaseUnitTest
import com.glpi.ifsp.hortolandia.data.model.Form
import com.glpi.ifsp.hortolandia.data.source.remote.ApiClient
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test
import retrofit2.Response

class FormRemoteRepositoryTest : BaseUnitTest() {

    @MockK
    private lateinit var api: ApiClient

    @InjectMockKs
    private lateinit var repository: FormRemoteRepository

    @Test
    fun `getForm - Check API call`() = runBlocking {
        coEvery { api().getForm(any(), any()) } returns mockResponse()

        repository.getForm("123", 1)

        coVerify { api().getForm("123", 1) }
    }

    private fun mockResponse(): Response<Form> {
        return Response.success(mockk())
    }
}

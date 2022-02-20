package com.glpi.ifsp.hortolandia.data.repository.location

import com.glpi.ifsp.hortolandia.BaseUnitTest
import com.glpi.ifsp.hortolandia.data.model.Location
import com.glpi.ifsp.hortolandia.data.source.remote.ApiClient
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test
import retrofit2.Response

class LocationRemoteRepositoryTest : BaseUnitTest() {

    @MockK
    private lateinit var api: ApiClient

    @InjectMockKs
    private lateinit var repository: LocationRemoteRepository

    @Test
    fun `getLocations - Check API call`() = runBlocking {
        coEvery { api().getLocations(any()) } returns mockResponse()

        repository.getLocations("123")

        coVerify { api().getLocations("123") }
    }

    private fun mockResponse(): Response<List<Location>> {
        return Response.success(mockk())
    }
}

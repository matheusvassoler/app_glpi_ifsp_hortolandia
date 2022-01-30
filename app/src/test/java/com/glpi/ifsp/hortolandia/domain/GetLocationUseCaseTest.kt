package com.glpi.ifsp.hortolandia.domain

import com.glpi.ifsp.hortolandia.BaseUnitTest
import com.glpi.ifsp.hortolandia.data.model.Location
import com.glpi.ifsp.hortolandia.data.repository.location.LocationRepository
import com.glpi.ifsp.hortolandia.infrastructure.exceptions.InternalErrorException
import com.glpi.ifsp.hortolandia.infrastructure.exceptions.ResponseRequestException
import com.glpi.ifsp.hortolandia.ui.model.LocationUI
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Test
import retrofit2.Response

class GetLocationUseCaseTest : BaseUnitTest() {

    @RelaxedMockK
    private lateinit var locationRepository: LocationRepository

    @RelaxedMockK
    private lateinit var sessionUseCase: SessionUseCase

    @InjectMockKs
    private lateinit var getLocationUseCase: GetLocationUseCase

    @Test
    fun `UseCase SHOULD return locations WHEN request is successful`() = runBlocking {
        // GIVEN
        val expectedResult = listOf(LocationUI(10, "Laboratórios", 0, hierarchicalLevelOfLocation = 1, subLocationsId = listOf(1, 2, 7, 8, 36), completeName = "Laboratórios"))
        coEvery { sessionUseCase.getSessionToken() } returns "12345"
        coEvery { locationRepository.getLocations(any()) } returns Response.success(mockResponse())

        // WHEN
        val result = getLocationUseCase()

        // THEN
        coVerify { locationRepository.getLocations("12345") }
        assertThat(result).isEqualTo(expectedResult)
    }

    @Test(expected = ResponseRequestException::class)
    fun `UseCase SHOULD throw ResponseRequestException WHEN get location was not successful`() = runBlocking {
        // GIVEN
        coEvery { sessionUseCase.getSessionToken() } returns "12345"
        coEvery { locationRepository.getLocations(any()) } returns Response.error(400, "".toResponseBody())

        // WHEN
        val result = getLocationUseCase()
    }

    @Test(expected = InternalErrorException::class)
    fun `UseCase SHOULD throw InternalErrorException WHEN session token is null`() = runBlocking {
        // GIVEN
        coEvery { sessionUseCase.getSessionToken() } returns null

        // WHEN
        val result = getLocationUseCase()
    }

    private fun mockResponse(): List<Location> {
        return listOf(
            Location(10, "Laboratórios", 0, hierarchicalLevelOfLocation = 1, subLocationsId = "{\"1\":\"1\",\"2\":\"2\",\"7\":\"7\",\"36\":\"36\",\"8\":\"8\"}", completeName = "Laboratórios")
        )
    }
}

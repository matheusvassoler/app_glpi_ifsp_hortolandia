package com.glpi.ifsp.hortolandia.domain

import com.glpi.ifsp.hortolandia.BaseUnitTest
import com.glpi.ifsp.hortolandia.data.enums.ItemType
import com.glpi.ifsp.hortolandia.data.model.Item
import com.glpi.ifsp.hortolandia.data.repository.item.ItemRepository
import com.glpi.ifsp.hortolandia.infrastructure.exceptions.InternalErrorException
import com.glpi.ifsp.hortolandia.infrastructure.exceptions.ResponseRequestException
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Test
import retrofit2.Response

class GetItemUseCaseTest : BaseUnitTest() {

    @RelaxedMockK
    private lateinit var itemRepository: ItemRepository

    @RelaxedMockK
    private lateinit var sessionUseCase: SessionUseCase

    @InjectMockKs
    private lateinit var getItemUseCase: GetItemUseCase

    @Test
    fun `UseCase SHOULD return locations WHEN request is successful`() = runBlocking {
        // GIVEN
        val expectedResult = listOf(Item(id = 1, "Impressora 1"))
        coEvery { sessionUseCase.getSessionToken() } returns "12345"
        coEvery { itemRepository.getItems(any(), any()) } returns Response.success(listOf(Item(id = 1, "Impressora 1")))

        // WHEN
        val result = getItemUseCase(ItemType.PRINTER)

        // THEN
        coVerify { itemRepository.getItems("12345", ItemType.PRINTER) }
        assertThat(result).isEqualTo(expectedResult)
    }

    @Test(expected = ResponseRequestException::class)
    fun `UseCase SHOULD throw ResponseRequestException WHEN get item was not successful`() = runBlocking {
        // GIVEN
        coEvery { sessionUseCase.getSessionToken() } returns "12345"
        coEvery { itemRepository.getItems(any(), any()) } returns Response.error(400, "".toResponseBody())

        // WHEN
        val result = getItemUseCase(ItemType.PRINTER)
    }

    @Test(expected = InternalErrorException::class)
    fun `UseCase SHOULD throw InternalErrorException WHEN session token is null`() = runBlocking {
        // GIVEN
        coEvery { sessionUseCase.getSessionToken() } returns null

        // WHEN
        val result = getItemUseCase(ItemType.PRINTER)
    }
}

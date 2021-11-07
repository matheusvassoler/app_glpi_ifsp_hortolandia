package com.glpi.ifsp.hortolandia.data.repository.item

import com.glpi.ifsp.hortolandia.BaseUnitTest
import com.glpi.ifsp.hortolandia.data.enums.ItemType
import com.glpi.ifsp.hortolandia.data.model.Item
import com.glpi.ifsp.hortolandia.data.source.remote.ApiClient
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test
import retrofit2.Response

class ItemRemoteRepositoryTest : BaseUnitTest() {

    @MockK
    private lateinit var api: ApiClient

    @InjectMockKs
    private lateinit var repository: ItemRemoteRepository

    @Test
    fun `getItens - Check API call`() = runBlocking {
        coEvery { api().getItems(any(), any()) } returns mockResponse()

        repository.getItems("123", ItemType.PRINTER)

        coVerify { api().getItems("123", "printer") }
    }

    private fun mockResponse(): Response<List<Item>> {
        return Response.success(mockk())
    }
}

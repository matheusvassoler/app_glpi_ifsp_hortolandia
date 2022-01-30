package com.glpi.ifsp.hortolandia.data.repository.item

import com.glpi.ifsp.hortolandia.data.enums.ItemType
import com.glpi.ifsp.hortolandia.data.model.Item
import com.glpi.ifsp.hortolandia.data.source.remote.ApiClient
import retrofit2.Response

class ItemRemoteRepository(private val api: ApiClient) : ItemRepository {

    override suspend fun getItems(sessionToken: String, itemType: ItemType): Response<List<Item>> {
        return api().getItems(sessionToken, itemType.name.lowercase())
    }
}

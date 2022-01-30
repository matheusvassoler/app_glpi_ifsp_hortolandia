package com.glpi.ifsp.hortolandia.data.repository.item

import com.glpi.ifsp.hortolandia.data.enums.ItemType
import com.glpi.ifsp.hortolandia.data.model.Item
import retrofit2.Response

interface ItemRepository {

    suspend fun getItems(sessionToken: String, itemType: ItemType): Response<List<Item>>
}

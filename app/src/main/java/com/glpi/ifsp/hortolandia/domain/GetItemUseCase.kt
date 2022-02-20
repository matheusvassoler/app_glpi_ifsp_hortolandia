package com.glpi.ifsp.hortolandia.domain

import com.glpi.ifsp.hortolandia.data.enums.ItemType
import com.glpi.ifsp.hortolandia.data.model.Item
import com.glpi.ifsp.hortolandia.data.repository.item.ItemRepository
import com.glpi.ifsp.hortolandia.infrastructure.exceptions.InternalErrorException
import com.glpi.ifsp.hortolandia.infrastructure.exceptions.ResponseRequestException

class GetItemUseCase(
    private val itemRepository: ItemRepository,
    private val sessionUseCase: SessionUseCase
) {

    suspend operator fun invoke(itemType: ItemType): List<Item> {
        val sessionToken = sessionUseCase.getSessionToken() ?: throw InternalErrorException()

        val response = itemRepository.getItems(sessionToken, itemType)

        if (response.isSuccessful) {
            return response.body() ?: listOf()
        } else {
            throw ResponseRequestException()
        }
    }
}

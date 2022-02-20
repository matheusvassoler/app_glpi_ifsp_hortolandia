package com.glpi.ifsp.hortolandia.domain

import com.glpi.ifsp.hortolandia.data.model.Location
import com.glpi.ifsp.hortolandia.data.repository.location.LocationRepository
import com.glpi.ifsp.hortolandia.infrastructure.exceptions.InternalErrorException
import com.glpi.ifsp.hortolandia.infrastructure.exceptions.ResponseRequestException
import com.glpi.ifsp.hortolandia.ui.model.LocationUI
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Response

class GetLocationUseCase(
    private val locationRepository: LocationRepository,
    private val sessionUseCase: SessionUseCase
) {

    suspend operator fun invoke(): List<LocationUI> {
        val sessionToken = sessionUseCase.getSessionToken() ?: throw InternalErrorException()

        val response = locationRepository.getLocations(sessionToken)

        if (response.isSuccessful) {
            return convertLocationsToLocationUI(response)
        } else {
            throw ResponseRequestException()
        }
    }

    private fun convertLocationsToLocationUI(response: Response<List<Location>>): ArrayList<LocationUI> {
        val locations = ArrayList<LocationUI>()
        response.body()?.let {
            for (location in it) {
                locations.add(
                    LocationUI(
                        id = location.id,
                        name = location.name,
                        parentLocation = location.parentLocation,
                        hierarchicalLevelOfLocation = location.hierarchicalLevelOfLocation,
                        subLocationsId = getSubLocationsId(location),
                        completeName = location.completeName
                    )
                )
            }
        }
        return locations
    }

    private fun getSubLocationsId(location: Location): List<Int>? {
        return if (location.subLocationsId != null) {
            val mapObj: Map<String, String> = getMapWithSubLocationsId(location)
            convertMapWithSubLocationsIdToList(mapObj)
        } else {
            null
        }
    }

    private fun convertMapWithSubLocationsIdToList(mapObj: Map<String, String>): List<Int> {
        val subLocationsId: ArrayList<Int> = ArrayList()
        for (subLocationIdString in mapObj.values) {
            subLocationsId.add(subLocationIdString.toInt())
        }

        return subLocationsId.sorted()
    }

    private fun getMapWithSubLocationsId(location: Location): Map<String, String> {
        return Gson().fromJson(
            location.subLocationsId,
            object : TypeToken<HashMap<String?, String?>?>() {}.type
        )
    }
}

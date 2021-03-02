package com.pose.composingclocks.feature.add

import com.pose.composingclocks.common.cities.CitiesListMockUseCase
import com.pose.composingclocks.common.cities.City
import com.pose.composingclocks.common.cities.mockData

class AddCityMockUseCase(private val citiesListMockUseCase: CitiesListMockUseCase) {
    operator fun invoke(city: City) {
        val list = citiesListMockUseCase.userCities.value
        val largestId = (if (list.isEmpty()) mockData else list).maxByOrNull { it.id }?.id ?: 0
        citiesListMockUseCase.userCities.value = list +
            city.copy(id = largestId + 1)
    }
}

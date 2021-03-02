package com.pose.composingclocks.feature.cities

import com.pose.composingclocks.common.cities.CitiesListMockUseCase
import com.pose.composingclocks.core.scopednav.ViewController
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.util.Calendar

class CitiesViewModel(
    citiesListMockUseCase: CitiesListMockUseCase,
    private val navToCity: (Int) -> Unit,
) : ViewController() {
    private val minutesTick: Flow<Unit> = flow {
        while (true) {
            emit(Unit)
            delay(60_000)
        }
    }
    val cities = citiesListMockUseCase()
        .map { city -> city.sortedBy { it.name } }
        .combine(minutesTick) { cities, _ ->
            cities.map { CityUiModel(it, Calendar.getInstance()) }
        }

    fun onCityClicked(city: CityUiModel) {
        navToCity(city.id)
    }
}

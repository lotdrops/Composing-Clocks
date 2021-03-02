package com.pose.composingclocks.feature.cities

import com.pose.composingclocks.common.cities.CitiesListMockUseCase
import com.pose.composingclocks.common.clock.TimezoneFormatter
import com.pose.composingclocks.core.scopednav.ViewController
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.util.TimeZone

class CityDetailViewModel(
    cityId: Int,
    citiesListMockUseCase: CitiesListMockUseCase,
    timezoneFormatter: TimezoneFormatter,
    private val navBack: () -> Unit
) : ViewController() {
    private val _city = citiesListMockUseCase().map { cities ->
        cities.first { it.id == cityId }
    }
    val city = _city.map { CityUiModel(it) }.stateIn(coroutineScope, SharingStarted.Lazily, null)
    val timezone = _city.map { timezoneFormatter.format(TimeZone.getTimeZone(it.timeZoneId)) }

    fun onBackClicked() {
        navBack()
    }
}

package com.pose.composingclocks.feature.cities

import com.pose.composingclocks.common.cities.City
import com.pose.composingclocks.common.clock.ClockType
import java.util.Calendar
import java.util.TimeZone

data class CityUiModel(
    val id: Int,
    val name: String,
    val description: String,
    val hours: Int,
    val minutes: Int,
    val image: String,
    val clockType: ClockType,
) {
    constructor(
        city: City,
        calendar: Calendar = Calendar.getInstance()
    ) : this(
        id = city.id,
        name = city.name,
        description = city.description,
        hours = calendar.getHour(city.timeZoneId),
        minutes = calendar.getMinute(city.timeZoneId),
        image = city.image,
        clockType = city.clockType,
    )
}

private fun Calendar.getHour(timezoneId: String) = this.apply {
    timeZone = TimeZone.getTimeZone(timezoneId)
}.get(Calendar.HOUR_OF_DAY)

private fun Calendar.getMinute(timezoneId: String) = this.apply {
    timeZone = TimeZone.getTimeZone(timezoneId)
}.get(Calendar.MINUTE)

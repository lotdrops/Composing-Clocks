package com.pose.composingclocks.feature.add

import com.pose.composingclocks.common.cities.City
import com.pose.composingclocks.common.clock.ClockType
import com.pose.composingclocks.core.savestate.StateSaver
import com.pose.composingclocks.core.scopednav.ViewController
import java.util.TimeZone

private const val NameKey = "AddCityNameStateSaverKey"
private const val ClockTypeKey = "AddCityClockTypeStateSaverKey"
private const val PhotoUrlKey = "AddCityPhotoUrlStateSaverKey"
private const val DescriptionKey = "AddCityDescriptionStateSaverKey"
private const val TimezoneKey = "AddCityTimezoneStateSaverKey"

class AddCityFlowViewModel(
    saver: StateSaver,
    private val saveUseCase: AddCityMockUseCase,
    private val onDone: () -> Unit,
) : ViewController() {

    val name = saver.getAutoSaveFlow(coroutineScope, NameKey, "")
    val clockType = saver.getAutoSaveFlow(coroutineScope, ClockTypeKey, ClockType.Minimalist)
    val photoUrl = saver.getAutoSaveFlow(coroutineScope, PhotoUrlKey, "")
    val description = saver.getAutoSaveFlow(coroutineScope, DescriptionKey, "")

    val timezones = TimeZone.getAvailableIDs()
        .asSequence()
        .filter {
            !it.startsWith("Etc/") && !it.startsWith("CST6") && !it.startsWith("EST") &&
                !it.startsWith("GMT") && it.length > 4
        }.map { TimeZone.getTimeZone(it).displayName.trim() to TimeZone.getTimeZone(it) }
        .filter { !it.first.startsWith("GMT") }
        .sortedWith(compareBy({ it.second.rawOffset }, { it.first }))
        .distinctBy { it.first }
        .map { it.second }
        .toList()
    private val defaultTimezone = timezones.indexOfFirst { it.rawOffset == 0 }
    val selectedTimezone = saver.getAutoSaveFlow(coroutineScope, TimezoneKey, defaultTimezone)

    fun onSave() {
        saveUseCase(
            City(
                id = 0,
                name = name.value,
                description = description.value,
                timeZoneId = timezones[selectedTimezone.value].id,
                image = photoUrl.value,
                clockType = clockType.value,
            )
        )
        onDone()
    }
}

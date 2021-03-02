package com.pose.composingclocks.common

import androidx.compose.ui.graphics.Color
import com.pose.composingclocks.common.clock.ClockTheme
import com.pose.composingclocks.core.savestate.StateSaver
import com.pose.composingclocks.core.scopednav.ViewController
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

private const val BottomBarKey = "BottomBar_AppStateSaverKey"
private const val LanguageKey = "Language_AppStateSaverKey"
private const val ClockThemeKey = "ClockTheme_AppStateSaverKey"

class AppViewModel(
    saver: StateSaver,
    bottomBarSelection: String,
) : ViewController() {
    val bottomBarSelection = saver.getAutoSaveFlow(coroutineScope, BottomBarKey, bottomBarSelection)

    val languagePosition = saver.getAutoSaveFlow(coroutineScope, LanguageKey, 0)
    val language = languagePosition
        .map { it.toLanguage() }
        .stateIn(coroutineScope, SharingStarted.Lazily, languagePosition.value.toLanguage())
    val currentClockTheme = saver.getAutoSaveFlow(coroutineScope, ClockThemeKey, ClockTheme())

    fun onBottomDestinationChanged(destination: String) {
        bottomBarSelection.value = destination
    }

    fun onRingColorSelected(color: Color) {
        currentClockTheme.value = currentClockTheme.value.copy(ringColor = color)
    }

    fun onHoursColorSelected(color: Color) {
        currentClockTheme.value = currentClockTheme.value.copy(hoursHandColor = color)
    }

    fun onMinutesColorSelected(color: Color) {
        currentClockTheme.value = currentClockTheme.value.copy(minutesHandColor = color)
    }

    fun onBackgroundColorSelected(color: Color) {
        currentClockTheme.value = currentClockTheme.value.copy(backgroundColor = color)
    }

    private fun Int.toLanguage() = if (this == 1) "es" else "en"
}

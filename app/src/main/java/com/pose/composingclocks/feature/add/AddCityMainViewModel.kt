package com.pose.composingclocks.feature.add

import com.pose.composingclocks.common.clock.TimezoneFormatter
import com.pose.composingclocks.core.scopednav.ViewController
import kotlinx.coroutines.flow.map

class AddCityMainViewModel(
    parentViewModel: AddCityFlowViewModel,
    private val nextScreen: () -> Unit,
    private val timezoneFormatter: TimezoneFormatter,
) : ViewController() {
    val name = parentViewModel.name
    val clockType = parentViewModel.clockType

    val timezoneNames = parentViewModel.timezones
        .map { timezoneFormatter.format(it) }
    val selectedTimezone = parentViewModel.selectedTimezone

    val buttonEnabled = name.map { it.trim().isNotEmpty() }

    fun onNextClick() {
        nextScreen()
    }
}

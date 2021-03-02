package com.pose.composingclocks.feature.add

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pose.composingclocks.R
import com.pose.composingclocks.common.clock.ClockType
import com.pose.composingclocks.common.widgets.ScreenTitle
import com.pose.composingclocks.common.widgets.SectionTitle
import com.pose.composingclocks.core.scopednav.navigation.NoParams
import com.pose.composingclocks.core.scopednav.navigation.ScreenDestination
import com.pose.composingclocks.feature.add.widgets.ClockSelector
import com.pose.composingclocks.feature.add.widgets.TimeZoneDropDown

object AddCityMainScreen : ScreenDestination<NoParams>(pathRoot = "addCityMainScreen")

@Composable
fun AddCityMainScreen(viewModel: AddCityMainViewModel) {
    val cityName = viewModel.name.collectAsState()
    val selectedTimezone = viewModel.selectedTimezone.collectAsState()
    val nextEnabled by viewModel.buttonEnabled.collectAsState(false)
    val clockType by viewModel.clockType.collectAsState()

    AddCityMainContent(
        cityName.value,
        { viewModel.name.value = it },
        nextEnabled,
        { viewModel.onNextClick() },
        viewModel.timezoneNames,
        selectedTimezone.value,
        { viewModel.selectedTimezone.value = it },
        clockType,
        { viewModel.clockType.value = it },
    )
}

@Composable
private fun AddCityMainContent(
    name: String,
    onNameChange: (String) -> Unit,
    nextEnabled: Boolean,
    onNext: () -> Unit,
    timezones: List<String>,
    selectedTimezone: Int,
    onTimezoneSelected: (Int) -> Unit,
    selectedType: ClockType,
    onTypeSelected: (ClockType) -> Unit,
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(colorResource(R.color.very_light_blue))
            .padding(16.dp)
    ) {
        ScreenTitle(text = stringResource(R.string.add_screen_title))

        TextField(
            value = name,
            onValueChange = onNameChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            label = { Text(text = stringResource(id = R.string.add_screen_city_name)) },
        )

        Spacer(modifier = Modifier.height(16.dp))

        TimeZoneDropDown(
            timezones,
            selectedTimezone,
            onTimezoneSelected,
        )

        Spacer(modifier = Modifier.height(16.dp))

        SectionTitle(
            text = stringResource(R.string.add_screen_clock_type),
            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp),
        )

        ClockSelector(
            selectedType,
            onTypeSelected,
            Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(vertical = 8.dp)
        )

        Button(
            onClick = onNext,
            enabled = nextEnabled,
            modifier = Modifier
                .padding(top = 24.dp, bottom = 8.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            Text(
                text = stringResource(id = R.string.add_screen_next_button),
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 32.dp),
            )
        }
    }
}

@Preview
@Composable
fun AddCityMainScreen() {
    var cityName = "City Name"
    val timezones = listOf("-1", "0", "1")
    var selectedTimezone = 1
    var selectedType = ClockType.Minimalist
    AddCityMainContent(
        cityName,
        { cityName = it },
        true,
        {},
        timezones,
        selectedTimezone,
        { selectedTimezone = it },
        selectedType,
        { selectedType = it },
    )
}

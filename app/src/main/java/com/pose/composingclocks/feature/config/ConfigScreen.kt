package com.pose.composingclocks.feature.config

import android.os.Bundle
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pose.composingclocks.R
import com.pose.composingclocks.common.AppViewModel
import com.pose.composingclocks.common.clock.ClockTheme
import com.pose.composingclocks.common.widgets.ScreenTitle
import com.pose.composingclocks.core.savestate.BundleStateSaver
import com.pose.composingclocks.core.scopednav.navigation.NoParams
import com.pose.composingclocks.core.scopednav.navigation.ScreenDestination

object ConfigScreen : ScreenDestination<NoParams>(pathRoot = "configScreen")

@ExperimentalAnimationApi
@Composable
fun ConfigScreen(vm: ConfigViewModel, appVm: AppViewModel) {
    val currentLanguage: Int by appVm.languagePosition.collectAsState()
    val clockTheme: ClockTheme by appVm.currentClockTheme.collectAsState()

    Column(
        Modifier
            .fillMaxSize()
            .background(colorResource(R.color.very_light_blue))
            .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 0.dp)
    ) {
        ScreenTitle(text = stringResource(R.string.config_screen_title))

        Column(Modifier.verticalScroll(rememberScrollState(0))) {
            Spacer(modifier = Modifier.height(8.dp))
            LanguagePicker(currentLanguage, { selected -> appVm.languagePosition.value = selected })

            Spacer(modifier = Modifier.height(16.dp))
            ColorPicker(
                stringResource(R.string.config_screen_clock_ring),
                clockTheme.ringColor,
                { appVm.onRingColorSelected(it) }
            )

            Spacer(modifier = Modifier.height(16.dp))
            ColorPicker(
                stringResource(R.string.config_screen_hour_hand),
                clockTheme.hoursHandColor,
                { appVm.onHoursColorSelected(it) }
            )

            Spacer(modifier = Modifier.height(16.dp))
            ColorPicker(
                stringResource(R.string.config_screen_minute_hand),
                clockTheme.minutesHandColor,
                { appVm.onMinutesColorSelected(it) }
            )

            Spacer(modifier = Modifier.height(16.dp))
            ColorPicker(
                stringResource(R.string.config_screen_clock_bg),
                clockTheme.backgroundColor,
                { appVm.onBackgroundColorSelected(it) }
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@ExperimentalAnimationApi
@Preview(showBackground = true)
@Composable
fun ConfigScreenPreview() {
    ConfigScreen(ConfigViewModel(), AppViewModel(BundleStateSaver(Bundle()), ""))
}

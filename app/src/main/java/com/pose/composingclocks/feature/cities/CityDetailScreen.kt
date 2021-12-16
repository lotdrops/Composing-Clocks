package com.pose.composingclocks.feature.cities

import android.os.Bundle
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import com.pose.composingclocks.R
import com.pose.composingclocks.app.LocalClockTheme
import com.pose.composingclocks.app.theme.typography
import com.pose.composingclocks.common.clock.ClockType
import com.pose.composingclocks.common.widgets.Clock
import com.pose.composingclocks.common.widgets.ScreenBack
import com.pose.composingclocks.common.widgets.SectionTitle
import com.pose.composingclocks.core.scopednav.navigation.NavArgument
import com.pose.composingclocks.core.scopednav.navigation.NavParams
import com.pose.composingclocks.core.scopednav.navigation.ScreenDestinationWithArgs

private val CityIdArg = NavArgument.Required("city_id", NavType.IntType)
data class CityDetailValues(val cityId: Int) : NavParams {
    override val list: List<String> get() = listOf(cityId.toString())
}
object CityDetailScreen : ScreenDestinationWithArgs<CityDetailValues>(
    pathRoot = "cityDetailScreen",
    arguments = listOf(CityIdArg),
) {
    fun getId(args: Bundle?): Int? = CityIdArg.getValue(args)
}

@Composable
fun CityDetailScreen(viewModel: CityDetailViewModel) {
    val city: CityUiModel? by viewModel.city.collectAsState()
    val timezone: String by viewModel.timezone.collectAsState("")
    city?.let { CityDetail(it, timezone) { viewModel.onBackClicked() } }
}

@Composable
private fun CityDetail(city: CityUiModel, timezone: String, onBack: () -> Unit) {
    Column(Modifier.background(MaterialTheme.colors.background)) {
        Header(city.name, city.image, onBack)
        Column(Modifier.verticalScroll(rememberScrollState(0))) {
            ClockSection(city, timezone)
            Divider(Modifier.padding(horizontal = 16.dp))
            SectionTitle(
                text = stringResource(R.string.add_screen_description_hint),
                modifier = Modifier.padding(top = 24.dp, bottom = 8.dp, start = 16.dp),
            )
            Text(
                city.description,
                Modifier.padding(start = 16.dp, end = 16.dp, bottom = 24.dp),
            )
        }
    }
}

@Composable
private fun Header(title: String, imageUrl: String, onBack: () -> Unit) {
    Box(
        Modifier
            .fillMaxWidth()
            .height(220.dp)
    ) {
        CityImage(url = imageUrl, description = title)
        TitleWithGradient(text = title, modifier = Modifier.align(Alignment.BottomStart))
        CompositionLocalProvider(LocalContentColor provides Color.White) {
            ScreenBack(onBack = onBack, Modifier.padding(top = 8.dp))
        }
    }
}

@Composable
private fun ClockSection(city: CityUiModel, timezone: String) {
    Row {
        Text(
            text = timezone,
            style = typography.body1,
            modifier = Modifier
                .padding(vertical = 8.dp, horizontal = 16.dp)
                .weight(1f)
                .align(Alignment.CenterVertically),
        )
        Clock(
            hour = city.hours,
            minutes = city.minutes,
            ringColor = LocalClockTheme.current.ringColor,
            hoursHandColor = LocalClockTheme.current.hoursHandColor,
            minutesHandColor = LocalClockTheme.current.minutesHandColor,
            backgroundColor = LocalClockTheme.current.backgroundColor,
            modifier = Modifier
                .padding(end = 16.dp, bottom = 16.dp, top = 16.dp)
                .width(120.dp)
                .height(120.dp),
            clockType = city.clockType,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ScreenPreview() {
    CityDetail(
        CityUiModel(0, "City Name", "desc", 10, 10, "", ClockType.Minimalist),
        "Greenwich",
        {}
    )
}

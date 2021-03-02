package com.pose.composingclocks.feature.cities

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pose.composingclocks.R
import com.pose.composingclocks.app.LocalClockTheme
import com.pose.composingclocks.common.cities.mockData
import com.pose.composingclocks.common.widgets.Clock
import com.pose.composingclocks.common.widgets.NoPhoto
import com.pose.composingclocks.common.widgets.ScreenTitle
import com.pose.composingclocks.core.scopednav.navigation.NoParams
import com.pose.composingclocks.core.scopednav.navigation.ScreenDestination
import dev.chrisbanes.accompanist.coil.CoilImage

object CitiesScreen : ScreenDestination<NoParams>(pathRoot = "citiesScreen")

@Composable
fun CitiesScreen(viewModel: CitiesViewModel) {
    val cities = viewModel.cities.collectAsState(emptyList())
    Column(Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp)) {
        ScreenTitle(text = stringResource(R.string.cities_screen_title))

        CitiesList(cityList = cities.value, onUserClick = { viewModel.onCityClicked(it) })
    }
}

@Composable
private fun CitiesList(cityList: List<CityUiModel>, onUserClick: (CityUiModel) -> Unit) {
    if (cityList.isNotEmpty()) {
        LazyColumn {
            item { Spacer(Modifier.height(8.dp)) }
            items(cityList) { city ->
                CityRow(city = city, onCityClick = onUserClick)
            }
            item { Spacer(Modifier.height(8.dp)) }
        }
    }
}

@Composable
private fun CityRow(city: CityUiModel, onCityClick: (CityUiModel) -> Unit) {
    Box(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clip(RoundedCornerShape(16.dp))
            .height(144.dp)
    ) {

        CityImage(
            url = city.image,
            description = city.name,
            modifier = Modifier.clickable(onClick = { onCityClick(city) })
        )
        CoilImage(
            data = city.image,
            contentDescription = city.name,
            modifier = Modifier
                .clickable(onClick = { onCityClick(city) }),
            contentScale = ContentScale.Crop,
            error = { NoPhoto() },
        )

        TitleWithGradient(text = city.name, modifier = Modifier.align(Alignment.BottomStart))

        Clock(
            hour = city.hours,
            minutes = city.minutes,
            ringColor = LocalClockTheme.current.ringColor,
            hoursHandColor = LocalClockTheme.current.hoursHandColor,
            minutesHandColor = LocalClockTheme.current.minutesHandColor,
            backgroundColor = LocalClockTheme.current.backgroundColor,
            modifier = Modifier
                .padding(end = 8.dp, bottom = 8.dp)
                .width(64.dp)
                .height(64.dp)
                .align(Alignment.BottomEnd),
            clockType = city.clockType,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CityRowPreview() {
    CityRow(city = CityUiModel(mockData[0]), onCityClick = {})
}

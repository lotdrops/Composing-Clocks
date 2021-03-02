package com.pose.composingclocks.feature.add.widgets

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.pose.composingclocks.R
import com.pose.composingclocks.app.LocalClockTheme
import com.pose.composingclocks.common.clock.ClockType
import com.pose.composingclocks.common.widgets.Clock

@Composable
fun ClockSelector(
    selectedType: ClockType,
    onTypeSelected: (ClockType) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier) {
        Row(
            Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            SelectableClock(
                ClockType.Minimalist,
                stringResource(id = R.string.add_screen_type_minimalist),
                selectedType,
                onTypeSelected,
                Modifier
                    .fillMaxHeight()
                    .weight(1f)
            )

            Spacer(modifier = Modifier.width(16.dp))

            SelectableClock(
                ClockType.Complete,
                stringResource(id = R.string.add_screen_type_complete),
                selectedType,
                onTypeSelected,
                Modifier
                    .fillMaxHeight()
                    .weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            SelectableClock(
                ClockType.Modern,
                stringResource(id = R.string.add_screen_type_modern),
                selectedType,
                onTypeSelected,
                Modifier
                    .fillMaxHeight()
                    .weight(1f)
            )

            Spacer(modifier = Modifier.width(16.dp))

            SelectableClock(
                ClockType.Thick,
                stringResource(id = R.string.add_screen_type_thick),
                selectedType,
                onTypeSelected,
                Modifier
                    .fillMaxHeight()
                    .weight(1f)
            )
        }
    }
}

@Composable
private fun SelectableClock(
    type: ClockType,
    title: String,
    selectedType: ClockType,
    onTypeSelected: (ClockType) -> Unit,
    modifier: Modifier
) {
    val selected = selectedType == type
    val borderColorState = animateColorAsState(
        MaterialTheme.colors.primary.copy(alpha = if (selected) 1f else 0f)
    )
    val elevationState = animateDpAsState(targetValue = if (selected) 16.dp else 4.dp)
    Card(
        modifier = modifier.clip(MaterialTheme.shapes.medium).clickable { onTypeSelected(type) },
        border = BorderStroke(2.dp, borderColorState.value),
        elevation = elevationState.value,
    ) {
        Column {
            Clock(
                hour = 10,
                minutes = 10,
                ringColor = LocalClockTheme.current.ringColor,
                hoursHandColor = LocalClockTheme.current.hoursHandColor,
                minutesHandColor = LocalClockTheme.current.minutesHandColor,
                backgroundColor = LocalClockTheme.current.backgroundColor,
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 0.dp)
                    .aspectRatio(1f)
                    .fillMaxWidth()
                    .weight(1f)
                    .align(Alignment.CenterHorizontally),
                type
            )

            Text(title, Modifier.padding(bottom = 8.dp).align(Alignment.CenterHorizontally))
        }
    }
}

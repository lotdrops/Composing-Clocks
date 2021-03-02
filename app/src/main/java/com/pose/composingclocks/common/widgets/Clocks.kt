package com.pose.composingclocks.common.widgets

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pose.composingclocks.common.clock.ClockType

@Composable
fun Clock(
    hour: Int,
    minutes: Int,
    ringColor: Color,
    hoursHandColor: Color,
    minutesHandColor: Color,
    backgroundColor: Color,
    modifier: Modifier = Modifier,
    clockType: ClockType = ClockType.Minimalist,
) {
    val showFourthsMarkers = clockType != ClockType.Minimalist
    val showHoursMarkers = clockType == ClockType.Complete || clockType == ClockType.Thick
    val hasRoundStrokes = clockType == ClockType.Minimalist || clockType == ClockType.Complete
    val (hoursHandThickness, minutesHandThickness) = when (clockType) {
        ClockType.Minimalist -> HandThickness.Thin to HandThickness.Thin
        ClockType.Complete -> HandThickness.Medium to HandThickness.Thin
        ClockType.Modern -> HandThickness.Thin to HandThickness.Thin
        ClockType.Thick -> HandThickness.Thick to HandThickness.Thick
    }
    GenericClock(
        hour = hour,
        minutes = minutes,
        ringColor = ringColor,
        hoursHandColor = hoursHandColor,
        minutesHandColor = minutesHandColor,
        backgroundColor = backgroundColor,
        modifier = modifier,
        hoursHandThickness = hoursHandThickness,
        minutesHandThickness = minutesHandThickness,
        hasRoundStrokes = hasRoundStrokes,
        showFourthsMarkers = showFourthsMarkers,
        showHoursMarkers = showHoursMarkers
    )
}

@Composable
private fun GenericClock(
    hour: Int,
    minutes: Int,
    ringColor: Color,
    hoursHandColor: Color,
    minutesHandColor: Color,
    backgroundColor: Color,
    modifier: Modifier = Modifier,
    hoursHandThickness: HandThickness = HandThickness.Medium,
    minutesHandThickness: HandThickness = HandThickness.Thin,
    hasRoundStrokes: Boolean = false,
    showFourthsMarkers: Boolean = true,
    showHoursMarkers: Boolean = true
) {
    val minuteAngle = (minutes % 60) * 360 / 60f
    val hourAngle = (hour % 12) * 360 / 12f + minuteAngle / 12f
    var oldMinuteAngle: Float by remember { mutableStateOf(minuteAngle) }
    var oldHourAngle: Float by remember { mutableStateOf(hourAngle) }

    val minuteIncrement = minuteAngle % 360 - oldMinuteAngle % 360
    oldMinuteAngle += (if (minuteIncrement < 0) 360 else 0) + minuteIncrement
    val minuteState = animateFloatAsState(targetValue = oldMinuteAngle)

    val hourIncrement = hourAngle % 360 - oldHourAngle % 360
    oldHourAngle += (if (hourIncrement < 0) 360 else 0) + hourIncrement
    val hourState = animateFloatAsState(targetValue = oldHourAngle)

    Canvas(modifier = modifier) {
        val middle = Offset(size.minDimension / 2, size.minDimension / 2)

        drawCircularClockBg(
            backgroundColor = backgroundColor,
            ringColor = ringColor,
            middle = middle
        )

        if (showHoursMarkers) {
            drawMarkerDots(
                amount = 12,
                dotSize = DotSize.Small,
                color = ringColor,
                middle = middle
            )
        }

        if (showFourthsMarkers) {
            drawMarkerDots(
                amount = 4,
                dotSize = DotSize.Medium,
                color = ringColor,
                middle = middle
            )
        }

        drawHand(
            color = minutesHandColor,
            middle = middle,
            angle = minuteState.value,
            cap = if (hasRoundStrokes) StrokeCap.Round else StrokeCap.Square,
            thickness = minutesHandThickness,
            length = HandLength.Long,
        )

        drawHand(
            color = hoursHandColor,
            middle = middle,
            angle = hourState.value,
            cap = if (hasRoundStrokes) StrokeCap.Round else StrokeCap.Square,
            thickness = hoursHandThickness,
            length = HandLength.Short,
        )

        drawCircle(
            color = ringColor,
            center = middle,
            radius = size.width / 30f,
        )
    }
}

private fun DrawScope.drawMarkerDots(
    amount: Int,
    dotSize: DotSize,
    color: Color,
    middle: Offset
) {
    val radius = size.minDimension / (if (dotSize == DotSize.Small) 150f else 60f)

    for (hour in 1..amount) {
        val angle = hour * 360 / amount.toFloat()

        withTransform(
            {
                rotate(angle, middle)
            },
            {
                drawCircle(
                    color = color,
                    center = Offset(size.minDimension / 2, size.minDimension / 30f),
                    radius = radius,
                    style = Fill,
                )
            }
        )
    }
}

private fun DrawScope.drawCircularClockBg(
    backgroundColor: Color,
    ringColor: Color,
    middle: Offset
) {
    val strokeWidth = size.minDimension / 100f
    val radius = size.minDimension / 2 - strokeWidth / 2

    drawCircle(
        color = backgroundColor,
        center = middle,
        radius = radius,
        style = Fill,
    )

    drawCircle(
        color = ringColor,
        center = middle,
        radius = radius,
        style = Stroke(strokeWidth),
    )
}

private fun DrawScope.drawHand(
    color: Color,
    middle: Offset,
    angle: Float,
    cap: StrokeCap,
    thickness: HandThickness,
    length: HandLength,
) {
    val handWidth = when (thickness) {
        HandThickness.Thin -> size.width / 44f
        HandThickness.Medium -> size.width / 33f
        HandThickness.Thick -> size.width / 25f
    }
    val handOffset = if (length == HandLength.Long) size.height / 15f else size.height / 5f

    withTransform(
        { rotate(angle, middle) },
        {
            drawLine(
                cap = cap,
                strokeWidth = handWidth,
                color = color,
                start = middle,
                end = Offset(size.minDimension / 2, handOffset)
            )
        }
    )
}

@ExperimentalFoundationApi
@Preview(showBackground = true)
@Composable
private fun ClockPreview() {
    val items = ClockType.values().toList()
    LazyVerticalGrid(cells = GridCells.Fixed(2)) {
        items(items.size) { index ->
            val type = items[index]
            Clock(
                hour = 1,
                minutes = 15,
                ringColor = Color.Black,
                hoursHandColor = Color.Gray,
                minutesHandColor = Color.Red,
                backgroundColor = Color.White,
                modifier = Modifier
                    .width(150.dp)
                    .height(150.dp),
                clockType = type,
            )
        }
    }
}

@Preview(widthDp = 250, heightDp = 300)
@Composable
private fun BasicAnimatedClockPreview() {
    var minutes by remember { mutableStateOf(15) }
    Column {
        GenericClock(
            hour = 1,
            minutes = minutes,
            ringColor = Color.Red,
            hoursHandColor = Color.Magenta,
            minutesHandColor = Color.Yellow,
            backgroundColor = Color.Green,
            modifier = Modifier
                .width(250.dp)
                .height(250.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { minutes += 10 }, Modifier.align(Alignment.CenterHorizontally)) {
            Text(text = "increment min")
        }
    }
}

private enum class HandThickness { Thin, Medium, Thick }
private enum class HandLength { Short, Long }
private enum class DotSize { Small, Medium }

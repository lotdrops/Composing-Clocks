package com.pose.composingclocks.feature.config

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.pose.composingclocks.R
import com.pose.composingclocks.app.LANDSCAPE_SIZE_THRESHOLD
import com.pose.composingclocks.common.widgets.SectionTitle

@ExperimentalAnimationApi
@Composable
fun ColorPicker(title: String, selectedColor: Color, onColorSelected: (Color) -> Unit) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE &&
        configuration.screenWidthDp > LANDSCAPE_SIZE_THRESHOLD
    val isSmallScreen = configuration.screenHeightDp < LANDSCAPE_SIZE_THRESHOLD &&
        configuration.screenWidthDp <= LANDSCAPE_SIZE_THRESHOLD

    Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), elevation = 0.dp) {
        if (isLandscape) {
            ColorPickerContentLandscape(
                title = title,
                isCompact = isSmallScreen,
                selectedColor = selectedColor,
                onColorSelected = onColorSelected,
            )
        } else {
            ColorPickerContentPortrait(
                title = title,
                isCompact = isSmallScreen,
                selectedColor = selectedColor,
                onColorSelected = onColorSelected,
            )
        }
    }
}

@ExperimentalAnimationApi
@Composable
fun ColorPickerContentPortrait(
    title: String,
    isCompact: Boolean,
    selectedColor: Color,
    onColorSelected: (Color) -> Unit,
) {
    Column(Modifier.padding(bottom = 8.dp)) {
        SectionTitle(
            title,
            modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp),
        )
        AnimatedVisibility(visible = !isCompact) {
            Text(
                stringResource(id = R.string.config_screen_clock_select),
                Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp),
            )
        }
        Row(Modifier.horizontalScroll(rememberScrollState(0))) {
            Spacer(modifier = Modifier.width(8.dp))
            getColors().forEach { color ->
                SelectableColor(
                    color,
                    selectedColor == color,
                    isCompact,
                    { onColorSelected(color) },
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
        }
    }
}

@Composable
fun ColorPickerContentLandscape(
    title: String,
    isCompact: Boolean,
    selectedColor: Color,
    onColorSelected: (Color) -> Unit
) {
    Row {
        Column {
            SectionTitle(
                title,
                modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp),
            )
            Text(
                stringResource(id = R.string.config_screen_clock_select),
                Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 16.dp),
            )
        }

        Row(
            Modifier
                .align(Alignment.CenterVertically)
                .horizontalScroll(rememberScrollState(0))
        ) {
            Spacer(modifier = Modifier.width(16.dp))
            getColors().forEach { color ->
                SelectableColor(
                    color,
                    color == selectedColor,
                    isCompact,
                    { onColorSelected(color) },
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
        }
    }
}

@Composable
fun SelectableColor(
    color: Color,
    isSelected: Boolean,
    isCompact: Boolean,
    onColorSelected: () -> Unit
) {
    val outerColor = if (color == Color.White) {
        MaterialTheme.colors.primary.copy(alpha = if (isSelected) 0.55f else 0.1f)
    } else color.copy(alpha = if (isSelected) 0.35f else 0f)
    val size = animateDpAsState(targetValue = if (isCompact) 48.dp else 56.dp)
    Box(
        Modifier
            .padding(8.dp)
            .size(size.value)
            .clip(CircleShape)
            .clickable { onColorSelected() }
            .background(outerColor)
    ) {
        Box(
            Modifier
                .align(Alignment.Center)
                .fillMaxSize()
                .padding(3.dp)
                .clip(CircleShape)
                .background(color)
        )
    }
}

@Composable
private fun getColors() = listOf(
    Color.Black,
    colorResource(R.color.selectable_color_dark_gray),
    colorResource(R.color.selectable_color_gray),
    Color.Gray,
    Color.Red,
    Color.Green,
    Color.Blue,
    Color.Magenta,
    Color.Yellow,
    Color.Cyan,
    colorResource(R.color.selectable_color_light_gray),
    Color.White,
)

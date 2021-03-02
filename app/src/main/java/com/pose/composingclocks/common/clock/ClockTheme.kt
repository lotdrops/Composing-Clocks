package com.pose.composingclocks.common.clock

import androidx.compose.ui.graphics.Color
import java.io.Serializable

data class ClockTheme(
    val ringColor: Color = Color.Black,
    val hoursHandColor: Color = Color.Gray,
    val minutesHandColor: Color = Color.Red,
    val backgroundColor: Color = Color.White,
) : Serializable

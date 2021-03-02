package com.pose.composingclocks.common.cities

import com.pose.composingclocks.common.clock.ClockType
import java.io.Serializable

data class City(
    val id: Int,
    val name: String,
    val description: String,
    val timeZoneId: String,
    val image: String,
    val clockType: ClockType = ClockType.Minimalist,
) : Serializable

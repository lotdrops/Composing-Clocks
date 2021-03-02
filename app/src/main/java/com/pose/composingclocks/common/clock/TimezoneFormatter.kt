package com.pose.composingclocks.common.clock

import java.util.TimeZone
import kotlin.math.absoluteValue

class TimezoneFormatter {
    fun format(timezone: TimeZone) =
        "${timezone.rawOffset.gmtFromMillis()} ${timezone.displayName.trim()}"

    private fun Int.gmtFromMillis() =
        "GMT ${(this / 3600000).formatHours()}:${this.millisToMinutes()}"

    private fun Int.formatHours() = when {
        this <= -10 -> "-"
        this < 0 -> "-0"
        this == 0 -> "   "
        this < 10 -> "+0"
        else -> "+"
    } + this.absoluteValue

    private fun Int.millisToMinutes() =
        ((this.absoluteValue / 3600000f - this.absoluteValue / 3600000) * 60).toInt().let { minutes ->
            if (minutes < 10) "0$minutes" else "$minutes"
        }
}

package com.kito.feature.schedule.presentation.components

import kotlinx.datetime.LocalTime

fun isClassUpcoming(
    startTime: String,
    windowMinutes: Int = 15,
    now: LocalTime
): Boolean {
    return try {
        val start = parseTime(startTime)
        val windowMinute = start.minute - windowMinutes
        val windowHour = if (windowMinute < 0) start.hour - 1 else start.hour
        val adjustedMinute = if (windowMinute < 0) windowMinute + 60 else windowMinute
        val windowStart = LocalTime(windowHour.coerceAtLeast(0), adjustedMinute.coerceIn(0, 59))
        now >= windowStart && now < start
    } catch (_: Exception) {
        false
    }
}

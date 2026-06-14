package com.kito.feature.schedule.presentation.components

import kotlinx.datetime.LocalTime

fun isClassOngoing(
    startTime: String,
    endTime: String,
    now: LocalTime
): Boolean {
    return try {
        val start = parseTime(startTime)
        val end = parseTime(endTime)
        now in start..end
    } catch (_: Exception) {
        false
    }
}

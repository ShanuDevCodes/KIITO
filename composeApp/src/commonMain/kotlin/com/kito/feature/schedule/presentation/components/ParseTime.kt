package com.kito.feature.schedule.presentation.components

import kotlinx.datetime.LocalTime

fun parseTime(time: String): LocalTime {
    val parts = time.split(":")
    return LocalTime(parts[0].toInt(), parts[1].toInt(), if (parts.size > 2) parts[2].toInt() else 0)
}

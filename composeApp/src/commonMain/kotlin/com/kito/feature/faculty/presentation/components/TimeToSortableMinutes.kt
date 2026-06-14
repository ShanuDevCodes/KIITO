package com.kito.feature.faculty.presentation.components

fun timeToSortableMinutes(time: String): Int {
    val parts = time.split(":")
    val hour = parts[0].toIntOrNull() ?: return Int.MAX_VALUE
    val minute = parts.getOrNull(1)?.toIntOrNull() ?: 0

    val hour24 = when {
        hour in 1..7 -> hour + 12
        hour in 8..12 -> hour
        hour >= 13 -> hour
        else -> hour
    }

    return hour24 * 60 + minute
}

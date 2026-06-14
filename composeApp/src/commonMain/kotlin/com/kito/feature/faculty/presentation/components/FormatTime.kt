package com.kito.feature.faculty.presentation.components

fun formatTime(time: String): String {
    val parts = time.split(":")
    val hour = parts[0].toIntOrNull() ?: return time
    val minute = parts.getOrNull(1) ?: "00"

    return when {
        hour in 1..7 -> "${hour}:${minute} PM"
        hour in 8..12 -> "${hour}:${minute} AM"
        hour >= 13 -> "${hour - 12}:${minute} PM"
        else -> time
    }
}

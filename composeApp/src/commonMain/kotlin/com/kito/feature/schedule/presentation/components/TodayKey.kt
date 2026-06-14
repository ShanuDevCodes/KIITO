package com.kito.feature.schedule.presentation.components

import com.kito.core.common.util.currentLocalDateTime
import kotlinx.datetime.isoDayNumber

fun todayKey(): String {
    val dt = currentLocalDateTime()
    return when (dt.dayOfWeek.isoDayNumber) {
        1 -> "MON"
        2 -> "TUE"
        3 -> "WED"
        4 -> "THU"
        5 -> "FRI"
        6 -> "SAT"
        7 -> "SUN"
        else -> "MON"
    }
}

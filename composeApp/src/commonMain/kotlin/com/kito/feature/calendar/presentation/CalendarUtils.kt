package com.kito.feature.calendar.presentation

import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.todayIn

object CalendarUtils {
    fun isToday(day: Int, displayMonth: Int, displayYear: Int): Boolean {
        val today = kotlin.time.Clock.System.todayIn(TimeZone.currentSystemDefault())
        return day == today.dayOfMonth &&
                displayMonth == today.monthNumber &&
                displayYear == today.year
    }

    fun formatDateKey(day: Int, month: Int, year: Int): String =
        "$year-${month.toString().padStart(2, '0')}-${day.toString().padStart(2, '0')}"

    fun daysInMonth(month: Int, year: Int): Int = when (month) {
        1, 3, 5, 7, 8, 10, 12 -> 31
        4, 6, 9, 11            -> 30
        2 -> if (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)) 29 else 28
        else                   -> 30
    }

    fun firstDayOfMonth(month: Int, year: Int): Int {
        val dow = LocalDate(year, month, 1).dayOfWeek.isoDayNumber
        return if (dow == 7) 0 else dow
    }

    fun getDayOfWeek(dateStr: String): Int {
        return try {
            val parts = dateStr.split("-")
            val dow = LocalDate(parts[0].toInt(), parts[1].toInt(), parts[2].toInt()).dayOfWeek.isoDayNumber
            if (dow == 7) 0 else dow
        } catch (e: Exception) { 0 }
    }
}

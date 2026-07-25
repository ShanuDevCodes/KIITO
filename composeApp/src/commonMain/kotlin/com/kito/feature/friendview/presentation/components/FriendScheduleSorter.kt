package com.kito.feature.friendview.presentation.components

import com.kito.feature.friendview.domain.model.FriendScheduleItem

fun List<FriendScheduleItem>.sortedChronologically(): List<FriendScheduleItem> {
    return this.sortedBy { item ->
        try {
            val parts = item.startTime.split(":")
            val hour = parts[0].toInt()
            val minute = parts[1].toInt()
            hour * 60 + minute
        } catch (e: Exception) {
            0
        }
    }
}

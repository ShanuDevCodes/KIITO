package com.kito.feature.attendance.presentation

sealed interface AttendanceListEvent {
    data object Refresh : AttendanceListEvent
    data class Login(val password: String) : AttendanceListEvent
    data object DismissLogin : AttendanceListEvent
    data object ClearSyncState : AttendanceListEvent
}
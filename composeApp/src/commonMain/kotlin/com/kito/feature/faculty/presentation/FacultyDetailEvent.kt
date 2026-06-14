package com.kito.feature.faculty.presentation

sealed interface FacultyDetailEvent {
    data class LoadDetail(val facultyId: Long) : FacultyDetailEvent
}

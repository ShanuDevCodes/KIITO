package com.kito.feature.faculty.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import org.koin.compose.koinInject

@Composable
fun FacultyDetailScreen(
    viewModel: FacultyDetailViewModel = koinInject(),
    facultyId: Long,
    onBack: () -> Unit
) {
    val syncState by viewModel.syncState.collectAsState()
    val faculty by viewModel.faculty.collectAsState()
    val schedule by viewModel.schedule.collectAsState()

    LaunchedEffect(facultyId) {
        viewModel.onEvent(FacultyDetailEvent.LoadDetail(facultyId))
    }

    FacultyDetailContent(
        syncState = syncState,
        faculty = faculty,
        schedule = schedule,
        onBack = onBack
    )
}
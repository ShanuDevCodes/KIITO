package com.kito.feature.schedule.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import org.koin.compose.koinInject

@Composable
fun ScheduleScreen(
    viewModel: ScheduleScreenViewModel = koinInject(),
    onBack: () -> Unit
) {
    val schedule by viewModel.weeklySchedule.collectAsState()

    ScheduleContent(
        schedule = schedule,
        onBack = onBack
    )
}

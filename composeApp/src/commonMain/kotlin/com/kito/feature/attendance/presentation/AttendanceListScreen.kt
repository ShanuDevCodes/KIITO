package com.kito.feature.attendance.presentation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import com.kito.core.platform.toast
import com.kito.core.presentation.components.state.SyncUiState
import dev.chrisbanes.haze.ExperimentalHazeApi
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class,
    ExperimentalHazeMaterialsApi::class, ExperimentalHazeApi::class
)
@Composable
fun AttendanceListScreen(
    viewModel: AttendanceListScreenViewModel = koinInject(),
) {
    val attendance by viewModel.attendance.collectAsState()
    val sapLoggedIn by viewModel.sapLoggedIn.collectAsState()
    val syncState by viewModel.syncState.collectAsState()
    val requiredAttendance by viewModel.requiredAttendance.collectAsState()
    val loginState by viewModel.loginState.collectAsState()
    val isOnline by viewModel.isOnline.collectAsState()
    val averageAttendancePercentage by viewModel.averageAttendancePercentage.collectAsState()
    val highestAttendancePercentage by viewModel.highestAttendancePercentage.collectAsState()
    val lowestAttendancePercentage by viewModel.lowestAttendancePercentage.collectAsState()

    val haptic = LocalHapticFeedback.current

    LaunchedEffect(Unit) {
        viewModel.syncEvents.collect { event ->
            when (event) {
                is SyncUiState.Success -> {
                    haptic.performHapticFeedback(HapticFeedbackType.Confirm)
                    toast("Sync completed")
                    viewModel.setSyncStateIdle()
                }
                is SyncUiState.Error -> toast(event.message)
                else -> toast("unknown error")
            }
        }
    }

    AttendanceListContent(
        state = AttendanceListUiState(
            attendance = attendance,
            sapLoggedIn = sapLoggedIn,
            syncState = syncState,
            loginState = loginState,
            requiredAttendance = requiredAttendance,
            isOnline = isOnline,
            averageAttendancePercentage = averageAttendancePercentage,
            highestAttendancePercentage = highestAttendancePercentage,
            lowestAttendancePercentage = lowestAttendancePercentage
        ),
        onEvent = { event ->
            when (event) {
                is AttendanceListEvent.Refresh -> {
                    if (isOnline) {
                        viewModel.refresh()
                    } else {
                        toast("No Internet Connection")
                    }
                }

                is AttendanceListEvent.Login -> {
                    viewModel.login(event.password)
                }

                is AttendanceListEvent.DismissLogin -> {
                    viewModel.setLoginStateIdle()
                }

                is AttendanceListEvent.ClearSyncState -> {
                    viewModel.setSyncStateIdle()
                }
            }
        }
    )
}

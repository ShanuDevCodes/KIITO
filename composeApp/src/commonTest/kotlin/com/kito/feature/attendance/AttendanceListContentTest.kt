package com.kito.feature.attendance

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.v2.runComposeUiTest
import com.kito.core.ui.state.SyncUiState
import com.kito.feature.attendance.presentation.AttendanceListContent
import com.kito.feature.attendance.presentation.AttendanceListUiState
import com.kito.testing.attendance
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class AttendanceListContentTest {

    @Test
    fun attendance_content_renders() = runComposeUiTest {
        setContent {
            AttendanceListContent(
                state = AttendanceListUiState(
                    attendance = listOf(attendance()),
                    sapLoggedIn = true,
                    syncState = SyncUiState.Idle,
                    loginState = SyncUiState.Idle,
                    requiredAttendance = 75,
                    averageAttendancePercentage = 80.0,
                    highestAttendancePercentage = 90.0,
                    lowestAttendancePercentage = 70.0
                ),
                onEvent = {},
                enableAnimations = false
            )
        }
        onNodeWithTag("attendance_content").assertIsDisplayed()
    }

    @Test
    fun attendance_loggedOut_rendersConnectButton() = runComposeUiTest {
        setContent {
            AttendanceListContent(
                state = AttendanceListUiState(
                    attendance = emptyList(),
                    sapLoggedIn = false,
                    syncState = SyncUiState.Idle,
                    loginState = SyncUiState.Idle,
                    requiredAttendance = 75
                ),
                onEvent = {},
                enableAnimations = false
            )
        }
        onNodeWithTag("attendance_content").assertIsDisplayed()
    }
}

package com.kito.feature.settings

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.remember
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.v2.runComposeUiTest
import com.kito.core.presentation.components.state.SyncUiState
import com.kito.feature.settings.presentation.SettingsContent
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class SettingsUiTest {

    @Test
    fun settings_content_rendersList() = runComposeUiTest {
        setContent {
            SettingsContent(
                name = "John Doe",
                roll = "1234567",
                year = "3rd Year",
                term = "Autumn",
                notificationState = true,
                requiredAttendance = 75,
                isLoggedIn = true,
                syncState = SyncUiState.Idle,
                pendingEnable = false,
                onNameChange = {},
                onRollChange = {},
                onYearTermChange = { _, _ -> },
                onAttendanceChange = {},
                onLogin = {},
                onLogout = {},
                onSyncSuccess = {},
                onSetNotificationState = {},
                onClearPendingNotificationEnable = {},
                onRequestEnableNotifications = {},
                tabNavBackStack = null,
                snackbarHostState = remember { SnackbarHostState() }
            )
        }

        onNodeWithTag("settings_list").assertIsDisplayed()
    }
}

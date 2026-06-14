package com.kito.feature.settings

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.remember
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.runComposeUiTest
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
                onEvent = {},
                tabNavBackStack = null,
                snackbarHostState = remember { SnackbarHostState() }
            )
        }

        onNodeWithTag("settings_list").assertIsDisplayed()
    }

    @Test
    fun settings_content_loggedOutRendersLogin() = runComposeUiTest {
        setContent {
            SettingsContent(
                name = "John Doe",
                roll = "1234567",
                year = "3rd Year",
                term = "Autumn",
                notificationState = true,
                requiredAttendance = 75,
                isLoggedIn = false,
                syncState = SyncUiState.Idle,
                pendingEnable = false,
                onEvent = {},
                tabNavBackStack = null,
                snackbarHostState = remember { SnackbarHostState() }
            )
        }

        onNodeWithText("Login").assertIsDisplayed()
        onNodeWithText("Login to SAP").assertIsDisplayed()
    }

    @Test
    fun settings_content_loggedInRendersLogout() = runComposeUiTest {
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
                onEvent = {},
                tabNavBackStack = null,
                snackbarHostState = remember { SnackbarHostState() }
            )
        }

        onNodeWithText("Logout").assertIsDisplayed()
        onNodeWithText("Logout of SAP").assertIsDisplayed()
    }
}

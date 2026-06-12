package com.kito.feature.calendar

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.v2.runComposeUiTest
import com.kito.feature.calendar.presentation.CalendarContent
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class CalendarUiTest {

    @Test
    fun calendar_content_rendersMainUI() = runComposeUiTest {
        setContent {
            CalendarContent(
                displayMonth = 6,
                displayYear = 2026,
                selectedDate = "2026-06-12",
                currentView = "Month",
                heatMode = false,
                showStats = false,
                isLoading = false,
                showAddModal = false,
                events = emptyList(),
                onPrevMonth = {},
                onNextMonth = {},
                onSetView = {},
                onToggleHeat = {},
                onToggleStats = {},
                onSelectDay = {},
                onSelectDate = {},
                onShowAddModal = {},
                onPrevDay = {},
                onNextDay = {}
            )
        }

        onNodeWithTag("calendar_content").assertIsDisplayed()
    }
}

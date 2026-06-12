package com.kito.feature.schedule

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.v2.runComposeUiTest
import com.kito.feature.schedule.presentation.ScheduleContent
import com.kito.feature.schedule.presentation.WeekDay
import com.kito.testing.scheduleItem
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class ScheduleUiTest {

    @Test
    fun schedule_content_rendersList() = runComposeUiTest {
        val scheduleMap = WeekDay.entries.associateWith { day ->
            listOf(
                scheduleItem(subject = "Maths ($day)", day = day.name)
            )
        }
        setContent {
            ScheduleContent(
                schedule = scheduleMap,
                onBack = {},
                enableAnimations = false
            )
        }

        onNodeWithTag("schedule_content").assertIsDisplayed()
    }

    @Test
    fun schedule_empty_rendersContent() = runComposeUiTest {
        val scheduleMap = WeekDay.entries.associateWith {
            emptyList<com.kito.feature.schedule.domain.model.ScheduleItem>()
        }
        setContent {
            ScheduleContent(
                schedule = scheduleMap,
                onBack = {},
                enableAnimations = false
            )
        }

        onNodeWithTag("schedule_content").assertIsDisplayed()
    }
}

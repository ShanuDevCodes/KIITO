package com.kito.feature.exam

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.v2.runComposeUiTest
import com.kito.feature.exam.presentation.UpcomingExamContent
import com.kito.testing.examSchedule
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class ExamUiTest {

    @Test
    fun exam_content_rendersList() = runComposeUiTest {
        val exams = listOf(
            examSchedule(subject = "Maths", date = "2026-06-20"),
            examSchedule(subject = "Science", date = "2026-06-21")
        )
        setContent {
            UpcomingExamContent(
                examModel = exams,
                onBack = {},
                enableAnimations = false
            )
        }

        onNodeWithTag("exam_list").assertIsDisplayed()
    }

    @Test
    fun exam_empty_rendersEmpty() = runComposeUiTest {
        setContent {
            UpcomingExamContent(
                examModel = emptyList(),
                onBack = {},
                enableAnimations = false
            )
        }

        onNodeWithTag("exam_empty").assertIsDisplayed()
    }
}

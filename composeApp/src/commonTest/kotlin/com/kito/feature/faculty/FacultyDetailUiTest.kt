package com.kito.feature.faculty

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.v2.runComposeUiTest
import com.kito.core.ui.state.SyncUiState
import com.kito.feature.faculty.domain.model.Faculty
import com.kito.feature.faculty.domain.model.FacultyScheduleSlot
import com.kito.feature.faculty.presentation.FacultyDetailContent
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class FacultyDetailUiTest {

    @Test
    fun facultyDetail_success_rendersContent() = runComposeUiTest {
        setContent {
            FacultyDetailContent(
                syncState = SyncUiState.Success,
                faculty = Faculty(id = 1L, name = "Dr. Amit Sen", email = "amit@kito.edu", officeRoom = "Lab 302"),
                schedule = listOf(
                    FacultyScheduleSlot(day = "Mon", startTime = "09:00", endTime = "10:00", room = "LH1", subject = "CN", batch = "A")
                ),
                onBack = {},
                enableAnimations = false
            )
        }
        onNodeWithTag("faculty_detail_content").assertIsDisplayed()
    }

    @Test
    fun facultyDetail_loading_rendersContent() = runComposeUiTest {
        setContent {
            FacultyDetailContent(
                syncState = SyncUiState.Loading,
                faculty = null,
                schedule = emptyList(),
                onBack = {},
                enableAnimations = false
            )
        }
        onNodeWithTag("faculty_detail_content").assertIsDisplayed()
    }
}

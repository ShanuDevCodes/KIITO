package com.kito.feature.faculty

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.v2.runComposeUiTest
import com.kito.core.presentation.components.state.SearchResultState
import com.kito.core.presentation.components.state.SyncUiState
import com.kito.feature.faculty.presentation.FacultyContent
import com.kito.testing.faculty
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class FacultyUiTest {

    @Test
    fun faculty_content_rendersList() = runComposeUiTest {
        val list = listOf(
            faculty(id = 1L, name = "Dr. Amit Sen"),
            faculty(id = 2L, name = "Dr. Priya Sharma")
        )
        setContent {
            FacultyContent(
                facultyList = list,
                searchResultState = SearchResultState.Idle,
                facultySearchResult = emptyList(),
                isOnline = true,
                syncState = SyncUiState.Success,
                onClearSearchResult = {},
                onGetSearchResult = {},
                onFacultyClick = {}
            )
        }

        onNodeWithTag("faculty_list").assertIsDisplayed()
    }

    @Test
    fun faculty_loading_rendersLoading() = runComposeUiTest {
        setContent {
            FacultyContent(
                facultyList = emptyList(),
                searchResultState = SearchResultState.Idle,
                facultySearchResult = emptyList(),
                isOnline = true,
                syncState = SyncUiState.Loading,
                onClearSearchResult = {},
                onGetSearchResult = {},
                onFacultyClick = {}
            )
        }

        onNodeWithTag("faculty_loading").assertIsDisplayed()
    }

    @Test
    fun faculty_empty_rendersEmpty() = runComposeUiTest {
        setContent {
            FacultyContent(
                facultyList = emptyList(),
                searchResultState = SearchResultState.Idle,
                facultySearchResult = emptyList(),
                isOnline = true,
                syncState = SyncUiState.Success,
                onClearSearchResult = {},
                onGetSearchResult = {},
                onFacultyClick = {}
            )
        }

        onNodeWithTag("faculty_empty").assertIsDisplayed()
    }
}

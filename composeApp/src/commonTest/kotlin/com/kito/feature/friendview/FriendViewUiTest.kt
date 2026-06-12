package com.kito.feature.friendview

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.v2.runComposeUiTest
import com.kito.feature.friendview.presentation.FriendViewContent
import com.kito.feature.schedule.presentation.WeekDay
import com.kito.testing.friendScheduleItem
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class FriendViewUiTest {

    @Test
    fun friendview_content_rendersList() = runComposeUiTest {
        val scheduleMap = WeekDay.entries.associateWith { day ->
            listOf(
                friendScheduleItem(subject = "Maths", day = day.name)
            )
        }
        setContent {
            FriendViewContent(
                selectedRoll = "22CS002",
                friendRolls = listOf("22CS002"),
                schedule = scheduleMap,
                onBack = {},
                onSelectFriend = {},
                onRemoveFriend = {},
                onAddFriend = {},
                enableAnimations = false
            )
        }

        onNodeWithTag("friendview_content").assertIsDisplayed()
    }

    @Test
    fun friendview_empty_rendersEmpty() = runComposeUiTest {
        setContent {
            FriendViewContent(
                selectedRoll = "",
                friendRolls = emptyList(),
                schedule = emptyMap(),
                onBack = {},
                onSelectFriend = {},
                onRemoveFriend = {},
                onAddFriend = {},
                enableAnimations = false
            )
        }

        onNodeWithTag("friendview_content").assertIsDisplayed()
        onNodeWithTag("friendview_empty").assertIsDisplayed()
    }
}

package com.kito.feature.gpa

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.v2.runComposeUiTest
import com.kito.feature.gpa.presentation.GPAContent
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class GpaUiTest {

    @Test
    fun gpa_content_rendersMainUI() = runComposeUiTest {
        setContent {
            GPAContent(
                selectedSemester = 4,
                selectedBranch = "CS",
                roll = "22CS001",
                onSemesterSelected = {},
                onBranchSelected = {},
                onBack = {},
                enableAnimations = false
            )
        }

        onNodeWithTag("gpa_content").assertIsDisplayed()
    }
}

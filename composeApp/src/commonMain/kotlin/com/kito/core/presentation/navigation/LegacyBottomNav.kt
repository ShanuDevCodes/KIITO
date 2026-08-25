package com.kito.core.presentation.navigation

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kito.core.presentation.navigation3.BottomBarTab
import dev.chrisbanes.haze.ExperimentalHazeApi
import dev.chrisbanes.haze.HazeInputScale
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials

/**
 * The original bottom navigation bar, as it was implemented inline inside MainUI
 * before the "bottom nav bar revamp" (see HomeBottomNav in BottomBar.kt). Kept
 * here verbatim, just extracted into its own composable, for rollback/reference.
 *
 * No behavior, layout, animation, or styling changes were made versus the
 * original inline implementation.
 */
@OptIn(ExperimentalHazeMaterialsApi::class, ExperimentalHazeApi::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun LegacyBottomNav(
    tabs: List<BottomBarTab>,
    selectedTabIndex: Int,
    onTabSelected: (BottomBarTab) -> Unit,
    hazeState: HazeState,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .padding(vertical = 10.dp, horizontal = 64.dp)
            .fillMaxWidth()
            .height(64.dp)
            .clip(CircleShape)
            .hazeEffect(state = hazeState, style = HazeMaterials.ultraThin()) {
                blurRadius = 15.dp
                noiseFactor = 0.05f
                inputScale = HazeInputScale.Auto
                alpha = 0.98f
            }
            .border(
                width = Dp.Hairline,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.5f),
                        Color.White.copy(alpha = 0.1f),
                    )
                ),
                shape = CircleShape
            )
    ) {
        val animatedSelectedTabIndex by animateFloatAsState(
            targetValue = selectedTabIndex.toFloat(),
            label = "animatedSelectedTabIndex",
            animationSpec = spring(
                stiffness = Spring.StiffnessLow,
                dampingRatio = Spring.DampingRatioLowBouncy,
            )
        )
        val animatedColor by animateColorAsState(
            targetValue = tabs[selectedTabIndex].color,
            label = "animatedColor",
            animationSpec = spring(
                stiffness = Spring.StiffnessLow,
            )
        )

        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {
            val tabWidth = size.width / tabs.size
            val centerOffset = tabWidth * animatedSelectedTabIndex + tabWidth / 2

            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        animatedColor.copy(alpha = 0.3f),
                        Color.Transparent
                    ),
                    center = Offset(centerOffset, size.height * 0.55f),
                    radius = tabWidth * 0.7f
                ),
                radius = tabWidth * 0.7f,
                center = Offset(centerOffset, size.height * 0.55f)
            )

            val path = Path().apply {
                addRoundRect(
                    RoundRect(
                        size.toRect(),
                        CornerRadius(size.height / 2f)
                    )
                )
            }
            val measure = PathMeasure()
            measure.setPath(path, false)
            drawPath(
                path = path,
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color.Transparent,
                        animatedColor.copy(alpha = 0.5f),
                        animatedColor,
                        animatedColor.copy(alpha = 0.5f),
                        Color.Transparent,
                    ),
                    startX = centerOffset - (tabWidth * 0.6f),
                    endX = centerOffset + (tabWidth * 0.6f),
                ),
                style = Stroke(width = 5f)
            )
        }

        BottomBarTabs(
            tabs = tabs,
            selectedTab = selectedTabIndex,
            onTabSelected = onTabSelected
        )
    }
}

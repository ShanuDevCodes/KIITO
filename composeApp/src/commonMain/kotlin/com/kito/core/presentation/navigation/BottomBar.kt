package com.kito.core.presentation.navigation


import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.kito.core.presentation.navigation3.BottomBarTab
import com.kito.core.presentation.navigation3.NavigationItems
import com.kito.core.presentation.theme.KitoTheme
import dev.chrisbanes.haze.ExperimentalHazeApi
import dev.chrisbanes.haze.HazeInputScale
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials
import dev.chrisbanes.haze.rememberHazeState

private val BAR_HEIGHT = 62.dp
private val BAR_INSET = 4.dp
private val ICON_SIZE = 28.dp

@OptIn(ExperimentalHazeMaterialsApi::class, ExperimentalHazeApi::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun HomeBottomNav(
    tabs: List<BottomBarTab>,
    selectedIndex: Int,
    onTabClick: (BottomBarTab) -> Unit,
    hazeState: HazeState = rememberHazeState(),
    modifier: Modifier = Modifier
) {
    val selectedColor by animateColorAsState(
        targetValue = tabs.getOrNull(selectedIndex)?.color ?: MaterialTheme.colorScheme.primary,
        label = "selectedTabColor",
        animationSpec = spring(stiffness = Spring.StiffnessLow)
    )
    val animatedSelectedIndex by animateFloatAsState(
        targetValue = selectedIndex.toFloat(),
        label = "animatedSelectedIndex",
        animationSpec = spring(stiffness = Spring.StiffnessLow, dampingRatio = Spring.DampingRatioLowBouncy)
    )
    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .height(BAR_HEIGHT)
            .clip(RoundedCornerShape(percent = 50))
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
                shape = RoundedCornerShape(percent = 50)
            )
    ) {
        val itemWidth = maxWidth / tabs.size.coerceAtLeast(1)
        val indicatorOffset = itemWidth * animatedSelectedIndex
        val density = LocalDensity.current
        val outlinePath = remember(maxWidth, maxHeight, density) {
            val widthPx = with(density) { maxWidth.toPx() }
            val heightPx = with(density) { maxHeight.toPx() }
            Path().apply {
                addRoundRect(
                    RoundRect(
                        Rect(0f, 0f, widthPx, heightPx),
                        CornerRadius(heightPx / 2f)
                    )
                )
            }
        }
        Canvas(modifier = Modifier.fillMaxSize()) {
            val tabWidth = size.width / tabs.size.coerceAtLeast(1)
            val centerOffset = tabWidth * animatedSelectedIndex + tabWidth / 2

            drawPath(
                path = outlinePath,
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color.Transparent,
                        selectedColor.copy(alpha = 0.5f),
                        selectedColor,
                        selectedColor.copy(alpha = 0.5f),
                        Color.Transparent,
                    ),
                    startX = centerOffset - (tabWidth * 0.6f),
                    endX = centerOffset + (tabWidth * 0.6f),
                ),
                style = Stroke(width = 5f)
            )
        }
        Box(
            modifier = Modifier
                .offset { IntOffset(indicatorOffset.roundToPx(), 0) }
                .width(itemWidth)
                .fillMaxHeight()
                .padding(BAR_INSET)
                .clip(RoundedCornerShape(percent = 50))
                .background(selectedColor.copy(alpha = 0.22f))
        )
        Row(
            modifier = Modifier.padding(BAR_INSET).fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            tabs.forEachIndexed { index, tab ->
                HomeBottomNavItem(
                    tab = tab,
                    isSelected = index == selectedIndex,
                    onClick = { onTabClick(tab) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun HomeBottomNavItem(
    tab: BottomBarTab,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val tabColor = tab.color
    val contentColor by animateColorAsState(
        targetValue = if (isSelected) {
            tabColor
        } else {
            Color.White.copy(alpha = 0.6f)
        },
        label = "tabContentColor"
    )
    Column(
        modifier = modifier
            .fillMaxHeight()
            .selectable(
                selected = isSelected,
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .padding(top = 6.dp, bottom = 7.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Box {
            Icon(
                imageVector = tab.icon,
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.size(ICON_SIZE)
            )
        }
        Text(
            text = tab.title,
            style = MaterialTheme.typography.bodySmall,
            color = contentColor,
            maxLines = 1
        )
    }
}

@Preview(widthDp = 393)
@Composable
private fun HomeBottomNavLightPreview() {
    KitoTheme(darkTheme = true) {
        HomeBottomNav(tabs = NavigationItems, selectedIndex = 0, onTabClick = {})
    }
}

@Preview(widthDp = 393)
@Composable
private fun HomeBottomNavDarkPreview() {
    KitoTheme(darkTheme = false) {
        HomeBottomNav(tabs = NavigationItems, selectedIndex = 1, onTabClick = {})
    }
}
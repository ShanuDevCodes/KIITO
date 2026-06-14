package com.kito.feature.schedule.presentation.components

import androidx.compose.foundation.pager.PagerState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.util.lerp
import kotlin.math.absoluteValue

fun Modifier.horizontalCarouselTransition(
    page: Int,
    pagerState: PagerState,
    scale: Float = 0.91f
): Modifier {
    return graphicsLayer {
        val pageOffset = (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction
        val scale = if (pagerState.pageCount > 1) {
            lerp(
                start = scale,
                stop = 1f,
                fraction = 1f - pageOffset.absoluteValue.coerceIn(0f, 1f)
            )
        } else {
            lerp(
                start = 1f,
                stop = 1f,
                fraction = 1f - pageOffset.absoluteValue.coerceIn(0f, 1f)
            )
        }
        scaleX = scale
        scaleY = scale
        alpha = lerp(
            start = 0.4f,
            stop = 1f,
            fraction = 1f - pageOffset.absoluteValue.coerceIn(0f, 1f)
        )
    }
}

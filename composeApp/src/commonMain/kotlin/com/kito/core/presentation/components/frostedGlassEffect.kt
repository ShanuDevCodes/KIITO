package com.kito.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

fun Modifier.frostedGlassEffect(
    shape: RoundedCornerShape,
    baseColor: Color = Color.White
): Modifier = this
    .background(
        brush = Brush.verticalGradient(
            colors = listOf(
                baseColor.copy(alpha = 0.18f),  // bright top (light hits glass here)
                baseColor.copy(alpha = 0.07f),  // darker middle
                baseColor.copy(alpha = 0.13f),  // slightly brighter bottom reflection
            )
        ),
        shape = shape
    )
    .border(
        width = 1.dp,
        brush = Brush.linearGradient(
            colorStops = arrayOf(
                0.00f to Color.White.copy(alpha = 0.90f),  // strong top-left highlight
                0.15f to Color.White.copy(alpha = 0.45f),
                0.45f to Color.White.copy(alpha = 0.08f),
                0.75f to Color.Transparent,
                1.00f to Color.White.copy(alpha = 0.15f),  // subtle bottom-right
            ),
            start = Offset(0f, 0f),
            end = Offset(400f, 600f)
        ),
        shape = shape
    )
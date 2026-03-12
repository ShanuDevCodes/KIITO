package com.kito.core.presentation.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.kashif_e.backdrop.backdrops.LayerBackdrop
import com.kashif_e.backdrop.backdrops.layerBackdrop
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeSource

// Mimics sine ease-in-out — the smoothest natural curve for looping motion
private val SineInOut = CubicBezierEasing(0.45f, 0.05f, 0.55f, 0.95f)

@Composable
fun GlowBackground(
    modifier: Modifier = Modifier,
) {
    val infinite = rememberInfiniteTransition(label = "glow")

    // --- Glow 1: Orange — drifts gently in top-left zone ---
    val glow1X by infinite.animateFloat(
        initialValue = 0.18f, targetValue = 0.28f,
        animationSpec = infiniteRepeatable(
            animation = tween(7200, easing = SineInOut),
            repeatMode = RepeatMode.Reverse
        ), label = "g1x"
    )
    val glow1Y by infinite.animateFloat(
        initialValue = 0.14f, targetValue = 0.24f,
        animationSpec = infiniteRepeatable(
            animation = tween(8400, easing = SineInOut),
            repeatMode = RepeatMode.Reverse
        ), label = "g1y"
    )
    val glow1Radius by infinite.animateFloat(
        initialValue = 0.78f, targetValue = 0.90f,
        animationSpec = infiniteRepeatable(
            animation = tween(9000, easing = SineInOut),
            repeatMode = RepeatMode.Reverse
        ), label = "g1r"
    )

    // --- Glow 2: Pink/Red — drifts gently in bottom-right zone ---
    val glow2X by infinite.animateFloat(
        initialValue = 0.72f, targetValue = 0.84f,
        animationSpec = infiniteRepeatable(
            animation = tween(8600, easing = SineInOut),
            repeatMode = RepeatMode.Reverse
        ), label = "g2x"
    )
    val glow2Y by infinite.animateFloat(
        initialValue = 0.74f, targetValue = 0.86f,
        animationSpec = infiniteRepeatable(
            animation = tween(7800, easing = SineInOut),
            repeatMode = RepeatMode.Reverse
        ), label = "g2y"
    )
    val glow2Radius by infinite.animateFloat(
        initialValue = 0.82f, targetValue = 0.94f,
        animationSpec = infiniteRepeatable(
            animation = tween(8200, easing = SineInOut),
            repeatMode = RepeatMode.Reverse
        ), label = "g2r"
    )

    // --- Glow 3: Violet — slow center drift with gentle alpha breathe ---
    val glow3X by infinite.animateFloat(
        initialValue = 0.42f, targetValue = 0.58f,
        animationSpec = infiniteRepeatable(
            animation = tween(10400, easing = SineInOut),
            repeatMode = RepeatMode.Reverse
        ), label = "g3x"
    )
    val glow3Y by infinite.animateFloat(
        initialValue = 0.38f, targetValue = 0.54f,
        animationSpec = infiniteRepeatable(
            animation = tween(11200, easing = SineInOut),
            repeatMode = RepeatMode.Reverse
        ), label = "g3y"
    )
    val glow3Alpha by infinite.animateFloat(
        initialValue = 0.07f, targetValue = 0.14f,
        animationSpec = infiniteRepeatable(
            animation = tween(9600, easing = SineInOut),
            repeatMode = RepeatMode.Reverse
        ), label = "g3a"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0B0B0F))
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {

            // Orange glow — top-left
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFFFF6A00).copy(alpha = 0.26f),
                        Color(0xFFFF6A00).copy(alpha = 0.08f),
                        Color.Transparent
                    ),
                    radius = size.minDimension * glow1Radius,
                    center = Offset(size.width * glow1X, size.height * glow1Y)
                ),
                radius = size.minDimension * glow1Radius,
                center = Offset(size.width * glow1X, size.height * glow1Y)
            )

            // Pink/red glow — bottom-right
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFFFF2D55).copy(alpha = 0.24f),
                        Color(0xFFFF2D55).copy(alpha = 0.07f),
                        Color.Transparent
                    ),
                    radius = size.minDimension * glow2Radius,
                    center = Offset(size.width * glow2X, size.height * glow2Y)
                ),
                radius = size.minDimension * glow2Radius,
                center = Offset(size.width * glow2X, size.height * glow2Y)
            )

            // Violet accent — center
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFF7B2FFF).copy(alpha = glow3Alpha),
                        Color.Transparent
                    ),
                    radius = size.minDimension * 0.72f,
                    center = Offset(size.width * glow3X, size.height * glow3Y)
                ),
                radius = size.minDimension * 0.72f,
                center = Offset(size.width * glow3X, size.height * glow3Y)
            )
        }
    }
}
package com.kito.core.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.chrisbanes.haze.ExperimentalHazeApi
import dev.chrisbanes.haze.HazeInputScale
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeTint
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials

@Composable
@OptIn(ExperimentalHazeMaterialsApi::class, ExperimentalHazeApi::class)
fun Modifier.customHaze(
    state: HazeState,
    tint: Color? = null,
): Modifier = this.hazeEffect(state = state, style = HazeMaterials.ultraThin()) {
    blurRadius  = 30.dp
    noiseFactor = 0.05f
    inputScale  = HazeInputScale.Auto
    alpha       = 0.98f
    if (tint != null) {
        tints = listOf(HazeTint(tint))
    }
}
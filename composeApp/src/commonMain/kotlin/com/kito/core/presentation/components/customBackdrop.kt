package com.kito.core.presentation.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kashif_e.backdrop.backdrops.LayerBackdrop
import com.kashif_e.backdrop.drawBackdrop
import com.kashif_e.backdrop.effects.blur
import com.kashif_e.backdrop.effects.lens
import com.kashif_e.backdrop.effects.vibrancy

fun Modifier.customBackdrop(
    backdrop: LayerBackdrop,
    cornerRadius: Dp = 24.dp,
): Modifier = this.drawBackdrop(
    backdrop = backdrop,
    shape = { RoundedCornerShape(cornerRadius) },
    effects = {
        blur(4.dp.toPx())
        vibrancy()
        lens(
            refractionHeight = 24.dp.toPx(),
            refractionAmount  = 32.dp.toPx(),
            chromaticAberration = false
        )
    }
)
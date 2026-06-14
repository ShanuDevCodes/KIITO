package com.kito.feature.faculty.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kito.core.designsystem.shimmer

@Composable
fun ScheduleShimmerItem() {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Box(
            Modifier
                .fillMaxWidth(0.6f)
                .height(14.dp)
                .shimmer()
        )
        Box(
            Modifier
                .fillMaxWidth(0.3f)
                .height(12.dp)
                .shimmer()
        )
        Box(
            Modifier
                .fillMaxWidth(0.4f)
                .height(12.dp)
                .shimmer()
        )
        Box(
            Modifier
                .fillMaxWidth(0.35f)
                .height(12.dp)
                .shimmer()
        )
    }
}

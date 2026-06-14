package com.kito.feature.exam.presentation

import androidx.compose.animation.Animatable
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import com.kito.core.common.util.formatDate
import com.kito.core.common.util.formatTo12Hour
import com.kito.core.designsystem.SharedExpandContainer
import com.kito.core.designsystem.UIColors
import com.kito.core.designsystem.meshGradient
import com.kito.core.presentation.components.animation.NoDataFoundAnimation
import com.kito.core.presentation.navigation3.Routes
import com.kito.feature.exam.domain.model.ExamSchedule
import dev.chrisbanes.haze.ExperimentalHazeApi
import dev.chrisbanes.haze.HazeInputScale
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials
import dev.chrisbanes.haze.rememberHazeState
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import androidx.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
import kotlin.random.Random
import kotlin.time.Clock


@OptIn(ExperimentalHazeMaterialsApi::class, ExperimentalMaterial3ExpressiveApi::class,
    ExperimentalHazeApi::class, ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class
)
@Composable
fun UpcomingExamScreen(
    viewmodel: UpcomingExamViewModel = koinInject(),
    onBack: () -> Unit
) {
    val examModel by viewmodel.exams.collectAsState()
    SharedExpandContainer(
        routeKey = Routes.ExamSchedule,
        backgroundColor = Color(0xFF121116),
    ) {
        UpcomingExamContent(examModel = examModel, onBack = onBack)
    }
}

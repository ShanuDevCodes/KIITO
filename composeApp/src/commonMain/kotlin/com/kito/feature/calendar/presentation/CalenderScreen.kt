package com.kito.feature.calendar.presentation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import com.kito.feature.calendar.domain.model.CalendarEvent
import com.kito.feature.calendar.domain.repository.CalendarRepository
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import com.kito.feature.calendar.presentation.components.AddEventModal
import com.kito.feature.calendar.presentation.components.AgendaView
import com.kito.feature.calendar.presentation.components.CalendarHeader
import com.kito.feature.calendar.presentation.components.DayView
import com.kito.feature.calendar.presentation.components.MonthView
import com.kito.feature.calendar.presentation.components.SelectedDayPanel
import com.kito.feature.calendar.presentation.components.StatsPanel
import com.kito.feature.calendar.presentation.components.UpcomingPanel
import com.kito.feature.calendar.presentation.components.WeekView
import org.koin.compose.koinInject
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun CalendarScreen(
    viewModel: CalendarViewModel = koinInject()
) {
    val displayMonth by viewModel.displayMonth.collectAsState()
    val displayYear  by viewModel.displayYear.collectAsState()
    val selectedDate by viewModel.selectedDate.collectAsState()
    val currentView  by viewModel.currentView.collectAsState()
    val heatMode     by viewModel.heatMode.collectAsState()
    val showStats    by viewModel.showStats.collectAsState()
    val isLoading    by viewModel.isLoading.collectAsState()
    val showAddModal by viewModel.showAddModal.collectAsState()
    val events       by viewModel.events.collectAsState()

    CalendarContent(
        displayMonth = displayMonth,
        displayYear = displayYear,
        selectedDate = selectedDate,
        currentView = currentView,
        heatMode = heatMode,
        showStats = showStats,
        isLoading = isLoading,
        showAddModal = showAddModal,
        events = events,
        onPrevMonth = viewModel::prevMonth,
        onNextMonth = viewModel::nextMonth,
        onSetView = viewModel::setView,
        onToggleHeat = viewModel::toggleHeat,
        onToggleStats = viewModel::toggleStats,
        onSelectDay = viewModel::selectDay,
        onSelectDate = viewModel::selectDate,
        onShowAddModal = viewModel::setShowAddModal,
        onPrevDay = viewModel::prevDay,
        onNextDay = viewModel::nextDay
    )
}

@Composable
fun CalendarContent(
    displayMonth: Int,
    displayYear: Int,
    selectedDate: String,
    currentView: String,
    heatMode: Boolean,
    showStats: Boolean,
    isLoading: Boolean,
    showAddModal: Boolean,
    events: List<CalendarEvent>,
    onPrevMonth: () -> Unit,
    onNextMonth: () -> Unit,
    onSetView: (String) -> Unit,
    onToggleHeat: () -> Unit,
    onToggleStats: () -> Unit,
    onSelectDay: (Int) -> Unit,
    onSelectDate: (String) -> Unit,
    onShowAddModal: (Boolean) -> Unit,
    onPrevDay: () -> Unit,
    onNextDay: () -> Unit
) {
    val haptic = LocalHapticFeedback.current
    var animKey by remember { mutableIntStateOf(0) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    listOf(Color(0xFF08060F), Color(0xFF0D0B18), Color(0xFF0A0A14), Color(0xFF060810))
                )
            )
            .semantics { testTag = "calendar_content" }
    ) {
        AmbientOrbs()

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 110.dp)
        ) {
            item {
                CalendarHeader(
                    month = displayMonth, year = displayYear,
                    currentView = currentView,
                    heatMode = heatMode, showStats = showStats, isLoading = isLoading,
                    onPrev  = { haptic.performHapticFeedback(HapticFeedbackType.ContextClick); onPrevMonth(); animKey++ },
                    onNext  = { haptic.performHapticFeedback(HapticFeedbackType.ContextClick); onNextMonth(); animKey++ },
                    onViewChange  = { onSetView(it) },
                    onHeatToggle  = { haptic.performHapticFeedback(HapticFeedbackType.ContextClick); onToggleHeat() },
                    onStatsToggle = { haptic.performHapticFeedback(HapticFeedbackType.ContextClick); onToggleStats() }
                )
            }

            item {
                AnimatedVisibility(
                    visible = showStats,
                    enter = expandVertically(spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessMediumLow)) + fadeIn(),
                    exit  = shrinkVertically(tween(200)) + fadeOut()
                ) {
                    val classCount = remember(events) { events.count { it.category == "class" } }
                    val examCount = remember(events) { events.count { it.category == "exam" } }
                    val labCount = remember(events) { events.count { it.category == "lab" } }
                    StatsPanel(
                        totalEvents = events.size,
                        classCount = classCount,
                        examCount = examCount,
                        labCount = labCount
                    )
                }
            }

            item {
                AnimatedContent(
                    targetState = currentView,
                    transitionSpec = { fadeIn(tween(200)) togetherWith fadeOut(tween(150)) }
                ) { view ->
                    when (view) {
                        "month"  -> MonthView(
                            events = events, animKey = animKey, heatMode = heatMode, selectedDate = selectedDate,
                            displayMonth = displayMonth, displayYear = displayYear,
                            onDayClick = { day ->
                                haptic.performHapticFeedback(HapticFeedbackType.ContextClick)
                                onSelectDay(day)
                            },
                            onSwipe = { dir ->
                                if (dir > 0) onPrevMonth() else onNextMonth()
                                animKey++
                            }
                        )
                        "week"   -> WeekView(
                            displayMonth = displayMonth,
                            displayYear = displayYear,
                            selectedDate = selectedDate,
                            events = events,
                            onSelectDate = onSelectDate,
                            onSetView = onSetView
                        )
                        "day"    -> DayView(
                            selectedDate = selectedDate,
                            selEvents = remember(events, selectedDate) { events.filter { it.date == selectedDate } },
                            getDayOfWeek = { CalendarUtils.getDayOfWeek(it) },
                            onPrevDay = onPrevDay,
                            onNextDay = onNextDay
                        )
                        "agenda" -> {
                            val agendaEvents = remember(events) {
                                val grouped = mutableMapOf<String, MutableList<CalendarEvent>>()
                                events.forEach { ev ->
                                    val d = ev.date
                                    if (d.isNotEmpty()) {
                                        grouped.getOrPut(d) { mutableListOf() }.add(ev)
                                    }
                                }
                                grouped.entries
                                    .sortedBy { it.key }
                                    .associate { it.key to it.value.toList() }
                            }
                            AgendaView(
                                agendaEvents = agendaEvents,
                                isToday = { CalendarUtils.isToday(it, displayMonth, displayYear) },
                                getDayOfWeek = { CalendarUtils.getDayOfWeek(it) }
                            )
                        }
                        else     -> MonthView(
                            events = events, animKey = animKey, heatMode = heatMode, selectedDate = selectedDate,
                            displayMonth = displayMonth, displayYear = displayYear,
                            onDayClick = { onSelectDay(it) }, onSwipe = {}
                        )
                    }
                }
            }

            if (currentView == "month") {
                item {
                    Spacer(Modifier.height(10.dp))
                    val selEvents = remember(events, selectedDate) { events.filter { it.date == selectedDate } }
                    SelectedDayPanel(selectedDate = selectedDate, selEvents = selEvents)
                }
                item {
                    Spacer(Modifier.height(10.dp))
                    val upcoming = remember(events) {
                        val todayStr = kotlin.time.Clock.System.todayIn(TimeZone.currentSystemDefault()).toString()
                        events.filter { it.date >= todayStr }
                            .sortedWith(compareBy { "${it.date}${it.startTime}" })
                            .take(4)
                    }
                    UpcomingPanel(upcoming = upcoming)
                }
            }
        }

        FloatingActionButton(
            onClick = { haptic.performHapticFeedback(HapticFeedbackType.ContextClick); onShowAddModal(true) },
            modifier = Modifier.align(Alignment.BottomEnd).padding(end = 20.dp, bottom = 32.dp),
            shape = RoundedCornerShape(18.dp),
            containerColor = CalendarColors.orange,
            contentColor = Color.Black,
            elevation = FloatingActionButtonDefaults.elevation(8.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add Event", modifier = Modifier.size(26.dp))
        }

        if (showAddModal) {
            AddEventModal(selectedDate = selectedDate, onDismiss = { onShowAddModal(false) })
        }
    }
}

@Composable
fun AmbientOrbs() {
    val infiniteTransition = rememberInfiniteTransition()
    val float by infiniteTransition.animateFloat(
        0f, 1f, infiniteRepeatable(tween(8000, easing = LinearEasing), RepeatMode.Reverse)
    )
    Box(
        Modifier.offset(x = (-80).dp, y = (-80 + float * 20).dp).size(300.dp)
            .background(Brush.radialGradient(listOf(CalendarColors.orange.copy(.06f), Color.Transparent)), CircleShape)
    )
    Box(
        Modifier.fillMaxWidth().wrapContentWidth(Alignment.End)
            .offset(x = 60.dp, y = (600 - float * 30).dp).size(240.dp)
            .background(Brush.radialGradient(listOf(CalendarColors.purple.copy(.05f), Color.Transparent)), CircleShape)
    )
}

@Preview
@Composable
fun CalendarContentPreview() {
    CalendarContent(
        displayMonth = 6,
        displayYear = 2026,
        selectedDate = "2026-06-12",
        currentView = "month",
        heatMode = false,
        showStats = false,
        isLoading = false,
        showAddModal = false,
        events = emptyList(),
        onPrevMonth = {},
        onNextMonth = {},
        onSetView = {},
        onToggleHeat = {},
        onToggleStats = {},
        onSelectDay = {},
        onSelectDate = {},
        onShowAddModal = {},
        onPrevDay = {},
        onNextDay = {}
    )
}
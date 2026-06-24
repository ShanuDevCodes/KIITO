package com.kito.feature.schedule.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kito.core.datastore.domain.repository.PrefsRepository
import com.kito.feature.schedule.domain.model.ScheduleItem
import com.kito.feature.schedule.domain.repository.ScheduleRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class ScheduleScreenViewModel(
    private val prefs: PrefsRepository,
    private val scheduleRepository: ScheduleRepository
): ViewModel() {
    @OptIn(ExperimentalCoroutinesApi::class)
    val weeklySchedule: StateFlow<Map<WeekDay, List<ScheduleItem>>> =
        prefs.userRollFlow
            .flatMapLatest { roll ->
                kotlinx.coroutines.flow.combine(
                    WeekDay.entries.map { day ->
                        scheduleRepository
                            .getScheduleForDay(
                                rollNo = roll,
                                day = day.apiValue
                            )
                            .map { list -> day to list }
                    }
                ) { results ->
                    results.toMap()
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyMap()
            )
}



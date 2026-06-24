package com.kito.feature.app.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kito.core.datastore.domain.repository.PrefsRepository
import com.kito.feature.schedule.domain.repository.ScheduleRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class AppViewModel(
    private val pref: PrefsRepository,
    private val scheduleRepository: ScheduleRepository
): ViewModel() {
    fun checkResetFix(){
        viewModelScope.launch {
            val isResetFixDone = pref.resetFixFlow.first()
            if(!isResetFixDone){
                scheduleRepository.deleteAllSections()
                pref.setResetDone()
            }
        }
    }
}



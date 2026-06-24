package com.kito.feature.attendance.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kito.core.connectivity.domain.repository.ConnectivityRepository
import com.kito.core.ui.state.SyncUiState
import com.kito.core.sync.domain.SyncUseCase
import com.kito.feature.attendance.domain.model.Attendance
import com.kito.feature.attendance.domain.model.AttendanceSummary
import com.kito.feature.attendance.domain.usecase.GetAttendanceSummaryUseCase
import com.kito.core.auth.domain.usecase.GetSapPasswordUseCase
import com.kito.core.auth.domain.usecase.IsSapLoggedInUseCase
import com.kito.core.auth.domain.usecase.SaveSapPasswordUseCase
import com.kito.core.datastore.domain.usecase.GetRequiredAttendanceUseCase
import com.kito.core.datastore.domain.repository.PrefsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.core.annotation.Provided

class AttendanceListScreenViewModel(
    private val getAttendanceSummary: GetAttendanceSummaryUseCase,
    private val getRequiredAttendance: GetRequiredAttendanceUseCase,
    private val prefs: PrefsRepository,
    private val getSapPassword: GetSapPasswordUseCase,
    private val isSapLoggedIn: IsSapLoggedInUseCase,
    private val saveSapPassword: SaveSapPasswordUseCase,
    private val appSyncUseCase: SyncUseCase,
    @Provided private val connectivityRepository: ConnectivityRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default,
): ViewModel(){

    val isOnline = connectivityRepository.isOnline
    private val _syncState = MutableStateFlow<SyncUiState>(SyncUiState.Idle)

    private val _syncEvents = MutableSharedFlow<SyncUiState>()
    val syncEvents: SharedFlow<SyncUiState> = _syncEvents

    fun refresh(){
        viewModelScope.launch(dispatcher) {
            _syncState.value = SyncUiState.Loading

            val roll = prefs.userRollFlow.first()
            val sapPassword = getSapPassword()
            val year = prefs.academicYearFlow.first()
            val term = prefs.termCodeFlow.first()

            val result = appSyncUseCase.syncAll(
                roll = roll,
                sapPassword = sapPassword,
                year = year,
                term = term
            )

            _syncState.value = result.fold(
                onSuccess = {
                    _syncEvents.emit(SyncUiState.Success)
                    SyncUiState.Success
                },
                onFailure = {
                    SyncUiState.Error(it.message ?: "Sync failed")
                }
            )
        }
    }

    fun setSyncStateIdle(){
        _syncState.value = SyncUiState.Idle
    }

    private val sapLoggedIn = isSapLoggedIn()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false
        )
    private val summary: StateFlow<AttendanceSummary> =
        getAttendanceSummary()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = AttendanceSummary.Empty
            )

    private val attendance: StateFlow<List<Attendance>> =
        summary
            .map { it.items }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )
    private val requiredAttendance = getRequiredAttendance()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = 0
        )

    private val _loginState = MutableStateFlow<SyncUiState>(SyncUiState.Idle)

    private val averageAttendancePercentage: StateFlow<Double> =
        summary.map { it.averagePercentage }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0.0)
    private val highestAttendancePercentage: StateFlow<Double> =
        summary.map { it.highestPercentage }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0.0)
    private val lowestAttendancePercentage: StateFlow<Double> =
        summary.map { it.lowestPercentage }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0.0)

    val uiState: StateFlow<AttendanceListUiState> = combine(
        isOnline,
        _syncState,
        sapLoggedIn,
        attendance,
        requiredAttendance,
        _loginState,
        averageAttendancePercentage,
        highestAttendancePercentage,
        lowestAttendancePercentage
    ) { array ->
        AttendanceListUiState(
            isOnline = array[0] as Boolean,
            syncState = array[1] as SyncUiState,
            sapLoggedIn = array[2] as Boolean,
            attendance = array[3] as List<Attendance>,
            requiredAttendance = array[4] as Int,
            loginState = array[5] as SyncUiState,
            averageAttendancePercentage = array[6] as Double,
            highestAttendancePercentage = array[7] as Double,
            lowestAttendancePercentage = array[8] as Double
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = AttendanceListUiState()
    )

    fun login(
        password: String
    ){
        viewModelScope.launch(dispatcher) {
            _loginState.value = SyncUiState.Loading
            val roll = prefs.userRollFlow.first()
            val year = prefs.academicYearFlow.first()
            val term = prefs.termCodeFlow.first()

            val result = appSyncUseCase.syncAll(
                roll = roll,
                sapPassword = password,
                year = year,
                term = term
            )

            _loginState.value = result.fold(
                onSuccess = {
                    saveSapPassword(password)
                    SyncUiState.Success
                },
                onFailure = {
                    SyncUiState.Error(it.message ?: "Sync failed")
                }
            )
        }
    }

    fun setLoginStateIdle(){
        _loginState.value = SyncUiState.Idle
    }
}





package com.kito.feature.attendance

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import com.kito.core.datastore.PrefsRepository
import com.kito.core.platform.ConnectivityObserver
import com.kito.core.platform.SecureStorage
import com.kito.core.presentation.components.state.SyncUiState
import com.kito.feature.attendance.domain.usecase.GetAttendanceSummaryUseCase
import com.kito.feature.attendance.presentation.AttendanceListScreenViewModel
import com.kito.testing.FakeAttendanceRepository
import com.kito.core.sync.domain.SyncUseCase
import com.kito.testing.attendance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okio.FileSystem
import okio.Path.Companion.toPath
import okio.SYSTEM
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class AttendanceListScreenViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val tempPath = "attendance_prefs_test.preferences_pb".toPath()
    private lateinit var prefsRepository: PrefsRepository
    private lateinit var datastoreScope: CoroutineScope

    private lateinit var repo: FakeAttendanceRepository
    private lateinit var secureStorage: SecureStorage
    private lateinit var spySyncUseCase: SpySyncUseCase
    private lateinit var vm: AttendanceListScreenViewModel

    class SpySyncUseCase : SyncUseCase {
        var syncAllRoll: String? = null
        var syncAllPassword: String? = null
        var syncAllYear: String? = null
        var syncAllTerm: String? = null
        var result = Result.success(Unit)

        override suspend fun scheduleSync(roll: String): Result<Unit> = Result.success(Unit)

        override suspend fun syncAll(
            roll: String,
            sapPassword: String,
            year: String,
            term: String
        ): Result<Unit> {
            syncAllRoll = roll
            syncAllPassword = sapPassword
            syncAllYear = year
            syncAllTerm = term
            return result
        }
    }

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        datastoreScope = CoroutineScope(testDispatcher + SupervisorJob())
        prefsRepository = PrefsRepository(
            PreferenceDataStoreFactory.createWithPath(
                scope = datastoreScope,
                produceFile = { tempPath }
            )
        )
        repo = FakeAttendanceRepository()
        secureStorage = SecureStorage()
        spySyncUseCase = SpySyncUseCase()
        vm = AttendanceListScreenViewModel(
            getAttendanceSummary = GetAttendanceSummaryUseCase(repo),
            prefs = prefsRepository,
            secureStorage = secureStorage,
            appSyncUseCase = spySyncUseCase,
            connectivityObserver = ConnectivityObserver(),
            dispatcher = testDispatcher,
        )
    }

    @AfterTest
    fun teardown() {
        datastoreScope.cancel()
        Dispatchers.resetMain()
        try {
            FileSystem.SYSTEM.delete(tempPath)
        } catch (_: Exception) {
            // ignore
        }
    }

    @Test
    fun attendance_initiallyEmpty() = runTest(testDispatcher) {
        advanceUntilIdle()
        assertEquals(emptyList(), vm.attendance.value)
    }

    @Test
    fun attendance_updatesWhenRepoEmits() = runTest(testDispatcher) {
        val job = launch { vm.attendance.collect {} }
        repo.emit(listOf(attendance(subjectCode = "CS101"), attendance(subjectCode = "CS102")))
        advanceUntilIdle()
        assertEquals(2, vm.attendance.value.size)
        assertEquals("CS101", vm.attendance.value[0].subjectCode)
        job.cancel()
    }

    @Test
    fun averagePercentage_derivedCorrectly() = runTest(testDispatcher) {
        val job = launch { vm.averageAttendancePercentage.collect {} }
        repo.emit(listOf(attendance(percentage = 60.0), attendance(percentage = 80.0)))
        advanceUntilIdle()
        assertEquals(70.0, vm.averageAttendancePercentage.value)
        job.cancel()
    }

    @Test
    fun highestAndLowest_correct() = runTest(testDispatcher) {
        val job1 = launch { vm.highestAttendancePercentage.collect {} }
        val job2 = launch { vm.lowestAttendancePercentage.collect {} }
        repo.emit(listOf(
            attendance(percentage = 50.0),
            attendance(percentage = 90.0),
            attendance(percentage = 70.0),
        ))
        advanceUntilIdle()
        assertEquals(90.0, vm.highestAttendancePercentage.value)
        assertEquals(50.0, vm.lowestAttendancePercentage.value)
        job1.cancel(); job2.cancel()
    }

    @Test
    fun syncState_initiallyIdle() = runTest(testDispatcher) {
        assertIs<SyncUiState.Idle>(vm.syncState.value)
    }

    @Test
    fun refresh_success_updatesSyncStateAndEmitsEvent() = runTest(testDispatcher) {
        prefsRepository.setUserRollNumber("123456")
        prefsRepository.setAcademicYear("2026")
        prefsRepository.setTermCode("010")
        secureStorage.saveSapPassword("pwd")

        val events = mutableListOf<SyncUiState>()
        val job = launch { vm.syncEvents.collect { events.add(it) } }

        vm.refresh()
        advanceUntilIdle()

        assertEquals("123456", spySyncUseCase.syncAllRoll)
        assertEquals("pwd", spySyncUseCase.syncAllPassword)
        assertEquals("2026", spySyncUseCase.syncAllYear)
        assertEquals("010", spySyncUseCase.syncAllTerm)

        assertIs<SyncUiState.Success>(vm.syncState.value)
        assertEquals(1, events.size)
        assertIs<SyncUiState.Success>(events[0])

        job.cancel()
    }

    @Test
    fun refresh_failure_updatesSyncStateWithError() = runTest(testDispatcher) {
        spySyncUseCase.result = Result.failure(Exception("network error"))

        vm.refresh()
        advanceUntilIdle()

        assertIs<SyncUiState.Error>(vm.syncState.value)
        assertEquals("network error", (vm.syncState.value as SyncUiState.Error).message)
    }

    @Test
    fun setSyncStateIdle_resetsState() = runTest(testDispatcher) {
        spySyncUseCase.result = Result.failure(Exception("network error"))
        vm.refresh()
        advanceUntilIdle()
        assertIs<SyncUiState.Error>(vm.syncState.value)

        vm.setSyncStateIdle()
        assertIs<SyncUiState.Idle>(vm.syncState.value)
    }

    @Test
    fun login_success_savesPasswordAndUpdatesState() = runTest(testDispatcher) {
        prefsRepository.setUserRollNumber("123456")
        prefsRepository.setAcademicYear("2026")
        prefsRepository.setTermCode("010")

        vm.login("new_password")
        advanceUntilIdle()

        assertEquals("123456", spySyncUseCase.syncAllRoll)
        assertEquals("new_password", spySyncUseCase.syncAllPassword)
        assertEquals("new_password", secureStorage.getSapPassword())
        assertIs<SyncUiState.Success>(vm.loginState.value)
    }

    @Test
    fun login_failure_doesNotSavePasswordAndSetsError() = runTest(testDispatcher) {
        spySyncUseCase.result = Result.failure(Exception("auth failed"))

        vm.login("new_password")
        advanceUntilIdle()

        assertEquals("", secureStorage.getSapPassword())
        assertIs<SyncUiState.Error>(vm.loginState.value)
        assertEquals("auth failed", (vm.loginState.value as SyncUiState.Error).message)
    }

    @Test
    fun setLoginStateIdle_resetsState() = runTest(testDispatcher) {
        spySyncUseCase.result = Result.failure(Exception("auth failed"))
        vm.login("pwd")
        advanceUntilIdle()
        assertIs<SyncUiState.Error>(vm.loginState.value)

        vm.setLoginStateIdle()
        assertIs<SyncUiState.Idle>(vm.loginState.value)
    }

    @Test
    fun requiredAttendance_updatesCorrectly() = runTest(testDispatcher) {
        val job = launch { vm.requiredAttendance.collect {} }
        prefsRepository.setRequiredAttendance(85)
        advanceUntilIdle()
        assertEquals(85, vm.requiredAttendance.value)
        job.cancel()
    }
}

package com.kito.feature.exam

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import com.kito.core.common.util.currentLocalDateTime
import com.kito.core.datastore.domain.repository.PrefsRepository
import com.kito.core.datastore.data.PrefsRepositoryImpl
import com.kito.core.ui.state.SyncUiState
import com.kito.feature.exam.domain.model.ExamSchedule
import com.kito.feature.exam.presentation.UpcomingExamViewModel
import com.kito.testing.FakeExamRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.plus
import okio.FileSystem
import okio.Path.Companion.toPath
import okio.SYSTEM
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class ExamViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val tempPath = "exam_prefs_test.preferences_pb".toPath()
    private lateinit var prefsRepository: PrefsRepository
    private lateinit var datastoreScope: CoroutineScope

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        datastoreScope = CoroutineScope(testDispatcher + SupervisorJob())
        prefsRepository = PrefsRepositoryImpl(
            PreferenceDataStoreFactory.createWithPath(
                scope = datastoreScope,
                produceFile = { tempPath }
            )
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

    private fun exam(subject: String, date: String, startTime: String, endTime: String) = ExamSchedule(
        subject = subject,
        subjectCode = "SUB101",
        date = date,
        day = "Monday",
        startTime = startTime,
        endTime = endTime,
        batch = "B1",
        branch = "CSE",
        semester = 5
    )

    @Test
    fun uiState_initiallyIdle() = runTest(testDispatcher) {
        val vm = UpcomingExamViewModel(prefsRepository, FakeExamRepository(), testDispatcher)
        assertIs<SyncUiState.Idle>(vm.uiState.value)
    }

    @Test
    fun exams_initiallyEmpty() = runTest(testDispatcher) {
        val vm = UpcomingExamViewModel(prefsRepository, FakeExamRepository(), testDispatcher)
        val job = launch { vm.exams.collect {} }
        advanceUntilIdle()
        assertTrue(vm.exams.value.isEmpty())
        job.cancel()
    }

    @Test
    fun refreshAfterInit_completes() = runTest(testDispatcher) {
        val vm = UpcomingExamViewModel(prefsRepository, FakeExamRepository(), testDispatcher)
        val job = launch { vm.uiState.collect {} }
        vm.getExamSchedule()
        advanceUntilIdle()
        assertTrue(vm.uiState.value !is SyncUiState.Loading)
        job.cancel()
    }

    @Test
    fun getExamSchedule_filtersAndSortsExamsCorrectly() = runTest(testDispatcher) {
        val now = currentLocalDateTime()
        val yesterdayDateStr = now.date.plus(-1, DateTimeUnit.DAY).toString()
        val tomorrowDateStr = now.date.plus(1, DateTimeUnit.DAY).toString()
        val dayAfterTomorrowDateStr = now.date.plus(2, DateTimeUnit.DAY).toString()

        val pastExam = exam("Past Math", yesterdayDateStr, "10:00:00", "13:00:00")
        val futureExam2 = exam("Future Chemistry", dayAfterTomorrowDateStr, "10:00:00", "13:00:00")
        val futureExam1 = exam("Future Physics", tomorrowDateStr, "09:00:00", "12:00:00")
        
        // Ongoing today
        val ongoingExam = exam("Ongoing CS", now.date.toString(), "00:00:00", "23:59:59")

        val repo = FakeExamRepository(listOf(pastExam, futureExam2, futureExam1, ongoingExam))
        val vm = UpcomingExamViewModel(prefsRepository, repo, testDispatcher)

        val job = launch { vm.exams.collect {} }
        advanceUntilIdle()

        val results = vm.exams.value
        assertEquals(3, results.size)
        
        // Assert chronological sorting: Ongoing CS (today) -> Physics (tomorrow) -> Chemistry (day after tomorrow)
        assertEquals("Ongoing CS", results[0].subject)
        assertEquals("Future Physics", results[1].subject)
        assertEquals("Future Chemistry", results[2].subject)

        job.cancel()
    }
}

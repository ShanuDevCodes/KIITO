package com.kito.feature.schedule

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import com.kito.core.datastore.domain.repository.PrefsRepository
import com.kito.core.datastore.data.PrefsRepositoryImpl
import com.kito.feature.schedule.presentation.ScheduleScreenViewModel
import com.kito.feature.schedule.presentation.WeekDay
import com.kito.testing.FakeScheduleRepository
import com.kito.testing.scheduleItem
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
import okio.FileSystem
import okio.Path.Companion.toPath
import okio.SYSTEM
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class ScheduleScreenViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val tempPath = "schedule_prefs_test.preferences_pb".toPath()
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

    @Test
    fun weeklySchedule_initiallyEmpty() = runTest(testDispatcher) {
        val vm = ScheduleScreenViewModel(prefsRepository, FakeScheduleRepository())
        val job = launch { vm.weeklySchedule.collect {} }
        advanceUntilIdle()
        assertTrue(vm.weeklySchedule.value.isEmpty() || vm.weeklySchedule.value.values.all { it.isEmpty() })
        job.cancel()
    }

    @Test
    fun weeklySchedule_containsAllDays_whenSubscribed() = runTest(testDispatcher) {
        prefsRepository.setUserRollNumber("123456")
        val items = listOf(scheduleItem(subject = "Maths"), scheduleItem(subject = "Physics"))
        val vm = ScheduleScreenViewModel(prefsRepository, FakeScheduleRepository(items))
        val job = launch { vm.weeklySchedule.collect {} }
        advanceUntilIdle()
        
        val map = vm.weeklySchedule.value
        assertEquals(WeekDay.entries.size, map.size)
        assertTrue(map.containsKey(WeekDay.MON))
        assertEquals(2, map[WeekDay.MON]?.size)
        assertEquals("Maths", map[WeekDay.MON]?.get(0)?.subject)
        
        job.cancel()
    }
}

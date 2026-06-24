package com.kito.feature.app

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import com.kito.core.datastore.domain.repository.PrefsRepository
import com.kito.core.datastore.data.PrefsRepositoryImpl
import com.kito.feature.app.presentation.AppViewModel
import com.kito.testing.FakeScheduleRepository
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
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class AppViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val tempPath = "app_prefs_test.preferences_pb".toPath()
    private lateinit var prefsRepository: PrefsRepository
    private lateinit var datastoreScope: CoroutineScope

    private lateinit var scheduleRepository: FakeScheduleRepository
    private lateinit var vm: AppViewModel

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
        scheduleRepository = FakeScheduleRepository()
        vm = AppViewModel(
            pref = prefsRepository,
            scheduleRepository = scheduleRepository
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
    fun checkResetFix_whenResetFixNotDone_deletesSectionsAndSetsDone() = runTest(testDispatcher) {
        assertFalse(prefsRepository.resetFixFlow.first())
        assertFalse(scheduleRepository.deleteAllSectionsCalled)

        vm.checkResetFix()
        advanceUntilIdle()

        assertTrue(scheduleRepository.deleteAllSectionsCalled)
        assertTrue(prefsRepository.resetFixFlow.first())
    }

    @Test
    fun checkResetFix_whenResetFixAlreadyDone_doesNotDeleteSections() = runTest(testDispatcher) {
        prefsRepository.setResetDone()
        advanceUntilIdle()
        assertTrue(prefsRepository.resetFixFlow.first())

        vm.checkResetFix()
        advanceUntilIdle()

        assertFalse(scheduleRepository.deleteAllSectionsCalled)
    }
}

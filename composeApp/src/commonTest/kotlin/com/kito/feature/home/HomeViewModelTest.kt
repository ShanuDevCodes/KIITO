package com.kito.feature.home

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import com.kito.core.datastore.domain.repository.PrefsRepository
import com.kito.core.datastore.data.PrefsRepositoryImpl
import com.kito.core.designsystem.StartupSyncGuard
import com.kito.core.platform.ConnectivityObserver
import com.kito.core.platform.SecureStorage
import com.kito.core.auth.domain.usecase.GetSapPasswordUseCase
import com.kito.core.auth.domain.usecase.IsSapLoggedInUseCase
import com.kito.core.auth.domain.usecase.SaveSapPasswordUseCase
import com.kito.feature.home.presentation.HomeViewModel
import com.kito.feature.home.presentation.HomeEvent
import com.kito.testing.FakeAttendanceRepository
import com.kito.testing.FakeConnectivityRepository
import com.kito.testing.FakeHomeRepository
import com.kito.testing.FakeScheduleRepository
import com.kito.testing.FakeSyncUseCase
import com.kito.testing.eventOrAd
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
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val tempPath = "home_prefs_test.preferences_pb".toPath()
    private lateinit var prefsRepository: PrefsRepository
    private lateinit var datastoreScope: CoroutineScope

    private lateinit var isSapLoggedInUseCase: IsSapLoggedInUseCase
    private lateinit var getSapPasswordUseCase: GetSapPasswordUseCase
    private lateinit var saveSapPasswordUseCase: SaveSapPasswordUseCase

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
        val secureStorage = SecureStorage()
        isSapLoggedInUseCase = IsSapLoggedInUseCase(secureStorage)
        getSapPasswordUseCase = GetSapPasswordUseCase(secureStorage)
        saveSapPasswordUseCase = SaveSapPasswordUseCase(secureStorage)
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

    private fun vm(homeRepo: FakeHomeRepository = FakeHomeRepository()) = HomeViewModel(
        prefs = prefsRepository,
        isSapLoggedInUseCase = isSapLoggedInUseCase,
        getSapPasswordUseCase = getSapPasswordUseCase,
        saveSapPasswordUseCase = saveSapPasswordUseCase,
        attendanceRepository = FakeAttendanceRepository(),
        scheduleRepository = FakeScheduleRepository(),
        homeRepository = homeRepo,
        appSyncUseCase = FakeSyncUseCase(),
        syncGuard = StartupSyncGuard(),
        connectivityRepository = FakeConnectivityRepository(),
        dispatcher = testDispatcher,
    )

    @Test
    fun ads_initiallyEmpty() = runTest(testDispatcher) {
        val v = vm()
        val job = launch { v.ads.collect {} }
        advanceUntilIdle()
        // FakeHomeRepository returns empty list → ads empty
        assertTrue(v.ads.value.isEmpty())
        job.cancel()
    }

    @Test
    fun ads_loadedFromRepo() = runTest(testDispatcher) {
        val v = vm(FakeHomeRepository(events = listOf(eventOrAd(1L), eventOrAd(2L))))
        val job = launch { v.ads.collect {} }
        advanceUntilIdle()
        assertEquals(2, v.ads.value.size)
        job.cancel()
    }

    @Test
    fun khaooGullyEnabled_falseByDefault() = runTest(testDispatcher) {
        val v = vm(FakeHomeRepository(khaooGullyEnabled = false))
        val job = launch { v.isKhaooGullyEnabled.collect {} }
        advanceUntilIdle()
        assertFalse(v.isKhaooGullyEnabled.value)
        job.cancel()
    }

    @Test
    fun khaooGullyEnabled_trueWhenRepoEnabled() = runTest(testDispatcher) {
        val v = vm(FakeHomeRepository(khaooGullyEnabled = true))
        val job = launch { v.isKhaooGullyEnabled.collect {} }
        advanceUntilIdle()
        assertTrue(v.isKhaooGullyEnabled.value)
        job.cancel()
    }

    @Test
    fun onEvent_updateDay_updatesDayState() = runTest(testDispatcher) {
        val v = vm()
        val job = launch { v.day.collect {} }
        v.onEvent(HomeEvent.UpdateDay("MON"))
        advanceUntilIdle()
        assertEquals("MON", v.day.value)
        job.cancel()
    }
}

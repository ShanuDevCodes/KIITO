package com.kito.feature.attendance

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import com.kito.core.datastore.PrefsRepository
import com.kito.feature.attendance.domain.usecase.GetUserSyncPreferencesUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
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

@OptIn(ExperimentalCoroutinesApi::class)
class GetUserSyncPreferencesUseCaseTest {

    private val testDispatcher = StandardTestDispatcher()
    private val tempPath = "sync_prefs_usecase_test.preferences_pb".toPath()
    private lateinit var prefsRepository: PrefsRepository
    private lateinit var datastoreScope: CoroutineScope
    private lateinit var useCase: GetUserSyncPreferencesUseCase

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
        useCase = GetUserSyncPreferencesUseCase(prefsRepository)
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
    fun getUserSyncPreferences_returnsSavedValues() = runTest(testDispatcher) {
        prefsRepository.setUserRollNumber("11223344")
        prefsRepository.setAcademicYear("2026")
        prefsRepository.setTermCode("011")
        advanceUntilIdle()

        val config = useCase()
        assertEquals("11223344", config.roll)
        assertEquals("2026", config.year)
        assertEquals("011", config.term)
    }
}

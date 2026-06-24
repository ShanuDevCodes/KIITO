package com.kito.core.datastore

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import com.kito.core.datastore.domain.repository.PrefsRepository
import com.kito.core.datastore.data.PrefsRepositoryImpl
import com.kito.core.datastore.domain.usecase.GetRequiredAttendanceUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
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
class GetRequiredAttendanceUseCaseTest {

    private val testDispatcher = StandardTestDispatcher()
    private val tempPath = "req_attendance_usecase_test.preferences_pb".toPath()
    private lateinit var prefsRepository: PrefsRepository
    private lateinit var datastoreScope: CoroutineScope
    private lateinit var useCase: GetRequiredAttendanceUseCase

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
        useCase = GetRequiredAttendanceUseCase(prefsRepository)
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
    fun getRequiredAttendance_defaultPercentage() = runTest(testDispatcher) {
        val result = useCase().first()
        assertEquals(75, result)
    }

    @Test
    fun getRequiredAttendance_customPercentage() = runTest(testDispatcher) {
        prefsRepository.setRequiredAttendance(90)
        advanceUntilIdle()
        val result = useCase().first()
        assertEquals(90, result)
    }
}

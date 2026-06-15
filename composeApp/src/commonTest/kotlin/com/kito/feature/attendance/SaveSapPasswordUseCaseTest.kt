package com.kito.feature.attendance

import com.kito.core.platform.SecureStorage
import com.kito.feature.attendance.domain.usecase.SaveSapPasswordUseCase
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SaveSapPasswordUseCaseTest {

    @Test
    fun saveSapPassword_savesPasswordCorrectly() = runTest {
        val secureStorage = SecureStorage()
        secureStorage.clearSapPassword()

        val useCase = SaveSapPasswordUseCase(secureStorage)
        val result = useCase("new_secure_pwd")

        assertTrue(result)
        assertEquals("new_secure_pwd", secureStorage.getSapPassword())
    }
}

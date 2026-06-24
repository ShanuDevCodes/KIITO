package com.kito.core.auth

import com.kito.core.platform.SecureStorage
import com.kito.core.auth.domain.usecase.IsSapLoggedInUseCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class IsSapLoggedInUseCaseTest {

    @Test
    fun isSapLoggedIn_returnsCorrectState() = runTest {
        val secureStorage = SecureStorage()
        secureStorage.clearSapPassword()

        val useCase = IsSapLoggedInUseCase(secureStorage)
        assertFalse(useCase().first())

        secureStorage.saveSapPassword("some_password")
        assertTrue(useCase().first())
    }
}

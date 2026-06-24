package com.kito.core.auth

import com.kito.core.platform.SecureStorage
import com.kito.core.auth.domain.usecase.GetSapPasswordUseCase
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class GetSapPasswordUseCaseTest {

    @Test
    fun getSapPassword_returnsSavedPassword() = runTest {
        val secureStorage = SecureStorage()
        secureStorage.clearSapPassword()
        secureStorage.saveSapPassword("my_sap_password")

        val useCase = GetSapPasswordUseCase(secureStorage)
        assertEquals("my_sap_password", useCase())
    }
}

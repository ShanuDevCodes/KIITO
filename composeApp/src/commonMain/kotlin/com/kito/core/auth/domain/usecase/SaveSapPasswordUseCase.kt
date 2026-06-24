package com.kito.core.auth.domain.usecase

import com.kito.core.platform.SecureStorage
import org.koin.core.annotation.Provided

/**
 * Clean domain boundary to securely save the SAP password.
 */
class SaveSapPasswordUseCase(
    @Provided private val secureStorage: SecureStorage
) {
    suspend operator fun invoke(password: String): Boolean = secureStorage.saveSapPassword(password)
}

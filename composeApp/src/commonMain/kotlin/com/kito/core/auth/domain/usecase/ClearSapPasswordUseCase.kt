package com.kito.core.auth.domain.usecase

import com.kito.core.platform.SecureStorage
import org.koin.core.annotation.Provided

/**
 * Clean domain boundary to securely clear the SAP password.
 */
class ClearSapPasswordUseCase(
    @Provided private val secureStorage: SecureStorage
) {
    suspend operator fun invoke() = secureStorage.clearSapPassword()
}

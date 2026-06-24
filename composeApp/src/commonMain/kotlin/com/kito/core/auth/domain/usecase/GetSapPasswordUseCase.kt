package com.kito.core.auth.domain.usecase

import com.kito.core.platform.SecureStorage
import org.koin.core.annotation.Provided

/**
 * Clean domain boundary to retrieve the secure SAP password.
 */
class GetSapPasswordUseCase(
    @Provided private val secureStorage: SecureStorage
) {
    suspend operator fun invoke(): String = secureStorage.getSapPassword()
}

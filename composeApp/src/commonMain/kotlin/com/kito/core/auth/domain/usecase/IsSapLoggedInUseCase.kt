package com.kito.core.auth.domain.usecase

import com.kito.core.platform.SecureStorage
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Provided

/**
 * Clean domain boundary to observe whether the user is logged into the SAP portal.
 */
class IsSapLoggedInUseCase(
    @Provided private val secureStorage: SecureStorage
) {
    operator fun invoke(): Flow<Boolean> = secureStorage.isLoggedInFlow
}

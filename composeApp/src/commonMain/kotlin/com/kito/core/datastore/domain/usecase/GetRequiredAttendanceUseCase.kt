package com.kito.core.datastore.domain.usecase

import com.kito.core.datastore.domain.repository.PrefsRepository
import kotlinx.coroutines.flow.Flow

/**
 * Clean domain boundary to retrieve the user's required attendance percentage.
 */
class GetRequiredAttendanceUseCase(
    private val prefs: PrefsRepository
) {
    operator fun invoke(): Flow<Int> = prefs.requiredAttendanceFlow
}

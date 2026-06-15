package com.kito.feature.attendance.domain.usecase

import com.kito.core.datastore.PrefsRepository
import kotlinx.coroutines.flow.Flow

/**
 * Clean domain boundary to retrieve the user's required attendance percentage.
 */
class GetRequiredAttendanceUseCase(
    private val prefs: PrefsRepository
) {
    operator fun invoke(): Flow<Int> = prefs.requiredAttendanceFlow
}

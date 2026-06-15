package com.kito.feature.attendance.domain.usecase

import com.kito.core.datastore.PrefsRepository
import com.kito.feature.attendance.domain.model.UserSyncConfig
import kotlinx.coroutines.flow.first

/**
 * Clean domain boundary to retrieve the roll number, academic year, and term code needed for sync.
 */
class GetUserSyncPreferencesUseCase(
    private val prefs: PrefsRepository
) {
    suspend operator fun invoke(): UserSyncConfig {
        return UserSyncConfig(
            roll = prefs.userRollFlow.first(),
            year = prefs.academicYearFlow.first(),
            term = prefs.termCodeFlow.first()
        )
    }
}

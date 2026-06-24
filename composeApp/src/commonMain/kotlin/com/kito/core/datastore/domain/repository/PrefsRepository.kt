package com.kito.core.datastore.domain.repository

import kotlinx.coroutines.flow.Flow

/**
 * Domain-level interface for accessing application settings and user preferences.
 * Adheres to Dependency Inversion by keeping datastore implementation details out of the domain/presentation layers.
 */
interface PrefsRepository {
    val notificationStateFlow: Flow<Boolean>
    val resetFixFlow: Flow<Boolean>
    val requiredAttendanceFlow: Flow<Int>
    val userNameFlow: Flow<String>
    val userRollFlow: Flow<String>
    val academicYearFlow: Flow<String>
    val termCodeFlow: Flow<String>
    val onBoardingFlow: Flow<Boolean>
    val userSetupDoneFlow: Flow<Boolean>
    val friendRollsFlow: Flow<List<String>>
    val selectedFriendRollFlow: Flow<String>

    suspend fun setUserName(username: String)
    suspend fun setUserRollNumber(rollNumber: String)
    suspend fun setUserSetupDone()
    suspend fun setOnboardingDone()
    suspend fun setAcademicYear(year: String)
    suspend fun setTermCode(term: String)
    suspend fun setRequiredAttendance(attendance: Int)
    suspend fun setResetDone()
    suspend fun setNotificationState(state: Boolean)
    suspend fun addFriendRoll(roll: String)
    suspend fun removeFriendRoll(roll: String)
    suspend fun setSelectedFriendRoll(roll: String)
    suspend fun clearSelectedFriend()
}

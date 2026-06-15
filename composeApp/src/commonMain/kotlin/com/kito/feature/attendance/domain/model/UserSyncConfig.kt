package com.kito.feature.attendance.domain.model

/**
 * Pure domain model representing user preferences required for initiating a sync.
 */
data class UserSyncConfig(
    val roll: String,
    val year: String,
    val term: String
)

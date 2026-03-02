package com.kito.core.network.supabase.model

import kotlinx.serialization.Serializable

@Serializable
data class CalendarEventModel(
    val id: Long? = null,
    val title: String? = null,
    val description: String? = null,
    val date: String? = null,
    val start_time: String? = null,
    val end_time: String? = null,
    val category: String? = null,
    val color: String? = null,
    val is_active: Boolean? = true
)
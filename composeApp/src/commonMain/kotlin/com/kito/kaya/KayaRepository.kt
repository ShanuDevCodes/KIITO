package com.kito.kaya

import com.kito.core.database.entity.SectionEntity
import com.kito.kaya.sensitive.KayaPortalClient

sealed class KayaResult {
    data class Success(val sections: List<SectionEntity>) : KayaResult()
    data class Error(val message: String) : KayaResult()
}

/** Thin wrapper over [KayaPortalClient], mirroring [com.kito.sap.SapRepository]. */
class KayaRepository(
    private val kayaClient: KayaPortalClient
) {
    suspend fun fetchTimetable(username: String, password: String): KayaResult =
        try {
            kayaClient.fetchTimetable(username, password)
        } catch (e: Exception) {
            KayaResult.Error(e.message ?: "Unknown error")
        }
}

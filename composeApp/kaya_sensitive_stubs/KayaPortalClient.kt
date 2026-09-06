package com.kito.kaya.sensitive

import com.kito.kaya.KayaResult

class KayaPortalClient {
    suspend fun fetchTimetable(username: String, password: String): KayaResult =
        KayaResult.Error("KAYA private sources are unavailable")
}

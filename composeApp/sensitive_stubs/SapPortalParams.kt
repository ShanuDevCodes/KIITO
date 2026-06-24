package com.kito.sap.sensitive

object SapPortalParams {
    fun getLoginParams(salt: String, username: String, password: String): Map<String, String> = emptyMap()
    fun getNavEvent1Params(): Map<String, String> = emptyMap()
    fun getNavEvent2Params(): Map<String, String> = emptyMap()
    fun getNavEvent3Params(): Map<String, String> = emptyMap()
    fun getInitQueue(extSid: String, placeholderId: String = "_loadingPlaceholder_"): String = ""
    fun getAttendanceQueue(
        extSid: String, yearKey: String, termKey: String,
        yearId: String, termId: String, buttonId: String
    ): String = ""
    fun wdBody(secureId: String, queue: String): Map<String, String> = emptyMap()
    fun safeEncode(s: String): String = ""
}

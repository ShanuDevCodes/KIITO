package com.kito.sap.sensitive

object SapPortalUrls {
    const val WD_HOST = "https://wdprd.kiituniversity.net:8001"
    const val WD_ENDPOINT = "/sap/bc/webdynpro/sap/ZWDA_HRIQ_ST_ATTENDANCE"

    fun getLoginPageUrl(): String = ""
    fun getLogoutUrl(): String = ""
    fun getNavEvent1Url(): String = ""
    fun getNavEvent2Url(): String = ""
    fun getNavEvent3Url(): String = ""
    fun getInitialAttendanceUrl(extSid: String, contextId: String): String = ""
}

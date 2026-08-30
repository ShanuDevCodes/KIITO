package com.kito.integration

import com.kito.core.platform.AppConfig
import com.kito.kaya.KayaResult
import com.kito.kaya.sensitive.KayaPortalClient
import com.kito.sap.AttendanceResult
import com.kito.sap.SapPortalClient
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertIs
import kotlin.test.assertTrue

class PortalLiveTest {
    private val username = System.getenv("PORTAL_TEST_USER")

    @Test
    fun sapLogin() = runBlocking {
        val password = System.getenv("SAP_TEST_PASSWORD")
        if (username.isNullOrBlank() || password.isNullOrBlank()) return@runBlocking

        AppConfig.portalBase = "https://kiitportal.kiituniversity.net"
        AppConfig.wdPath = "/sap/bc/webdynpro/sap/ZWDA_HRIQ_ST_ATTENDANCE"

        val result = SapPortalClient().fetchAttendance(username, password)
        val success = assertIs<AttendanceResult.Success>(result, "SAP live login failed: $result")
        assertTrue(success.data.subjects.isNotEmpty(), "SAP returned no attendance subjects")
        println("SAP live check: ${success.data.subjects.size} subjects")
    }

    @Test
    fun kayaLogin() = runBlocking {
        val password = System.getenv("KAYA_TEST_PASSWORD")
        if (username.isNullOrBlank() || password.isNullOrBlank()) return@runBlocking

        val result = KayaPortalClient().fetchTimetable(username, password)
        val success = assertIs<KayaResult.Success>(result, "KAYA live login failed: $result")
        assertTrue(success.sections.isNotEmpty(), "KAYA returned no timetable rows")
        println("KAYA live check: ${success.sections.size} timetable rows")
    }
}

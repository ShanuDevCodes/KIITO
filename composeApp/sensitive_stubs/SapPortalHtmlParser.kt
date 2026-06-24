package com.kito.sap.sensitive

import com.kito.sap.SubjectAttendance

object SapPortalHtmlParser {
    fun extractSaltFromLoginPage(loginPageHtml: String?): String? = null
    fun extractWebDynproFormAction(nav3Html: String?): String? = null
    fun extractFormFields(wdFormHtml: String): Map<String, String> = emptyMap()
    fun detectAcademicYearAndTerm(wdResponseHtml: String?, academicYear: String, termCode: String): Pair<String, String> = Pair("", "")
    fun detectComboboxIds(html: String): Triple<String, String, String> = Triple("", "", "")
    fun parseAttendanceData(rawResponse: String): List<SubjectAttendance> = emptyList()
}

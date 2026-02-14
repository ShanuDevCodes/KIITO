package com.kito.sap

import com.fleeksoft.ksoup.Ksoup
import com.kito.core.platform.AppConfig
import com.kito.core.platform.createHttpEngine
import com.kito.sap.sensitive.SapPortalHeaders
import com.kito.sap.sensitive.SapPortalHtmlParser
import com.kito.sap.sensitive.SapPortalParams
import com.kito.sap.sensitive.SapPortalTokenExtractor
import com.kito.sap.sensitive.SapPortalUrls
import io.ktor.client.HttpClient
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpRedirect
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.cookies.AcceptAllCookiesStorage
import io.ktor.client.plugins.cookies.HttpCookies
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.headers
import io.ktor.client.statement.bodyAsText
import io.ktor.http.Parameters
import io.ktor.http.decodeURLPart
import io.ktor.http.encodeURLQueryComponent
import io.ktor.http.isSuccess

class SapPortalClient {

    private fun createClient(): HttpClient {
        return HttpClient(createHttpEngine()) {
            install(HttpCookies) {
                storage = AcceptAllCookiesStorage()
            }
            install(HttpRedirect) {
                checkHttpMethod = false // Keep the same method for redirects (needed for some poorly behaved servers, mirroring OkHttp aggression)
            }
            install(HttpTimeout) {
                requestTimeoutMillis = 30000
                connectTimeoutMillis = 30000
                socketTimeoutMillis = 30000
            }
            install(DefaultRequest) {
                header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/141.0.0.0 Safari/537.36")
                header("sec-ch-ua", "\"Google Chrome\";v=\"141\", \"Not?A_Brand\";v=\"8\", \"Chromium\";v=\"141\"")
                header("sec-ch-ua-mobile", "?0")
                header("sec-ch-ua-platform", "\"Windows\"")
                header("sec-fetch-dest", "document")
                header("sec-fetch-mode", "navigate")
                header("sec-fetch-site", "same-origin")
                header("sec-fetch-user", "?1")
                header("upgrade-insecure-requests", "1")
                header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7")
                header("Accept-Language", "en-US,en;q=0.9")
                header("Cache-Control", "no-cache")
                header("Pragma", "no-cache")
                header("DNT", "1")
            }
        }
    }

    suspend fun fetchAttendance(username: String, password: String, academicYear: String = "", termCode: String = ""): AttendanceResult {
        val client = createClient()
        
        try {
            // Step 1: Load login page
            val loginPageResponse = client.get(SapPortalUrls.getLoginPageUrl())

            if (!loginPageResponse.status.isSuccess()) {
                return AttendanceResult.Error("Failed to load login page. Status: ${loginPageResponse.status.value}")
            }

            val loginPageHtml = loginPageResponse.bodyAsText()
            val salt = SapPortalHtmlParser.extractSaltFromLoginPage(loginPageHtml)

            if (salt.isNullOrEmpty()) {

                return AttendanceResult.Error("Could not extract j_salt from login page")
            }

            // Step 2: Submit login
            val loginParams = SapPortalParams.getLoginParams(salt, username, password)
            val loginResponse = client.submitForm(
                url = SapPortalUrls.getLoginPageUrl(),
                formParameters = Parameters.build {
                    loginParams.forEach { (key, value) -> append(key, value) }
                }
            ) {
                header("content-type", "application/x-www-form-urlencoded")
            }

            if (!loginResponse.status.isSuccess()) {
                val responseContent = loginResponse.bodyAsText()
                return AttendanceResult.Error("Login failed with status: ${loginResponse.status.value}. Response: ${responseContent.take(200)}")
            }
            secureWipe(username.toCharArray())
            secureWipe(password.toCharArray())

            // Check if login was successful
            val loginResultHtml = loginResponse.bodyAsText()
            if (loginResultHtml.contains("authentication failed", true)) {
                return AttendanceResult.Error("Invalid credentials")
            }

            // Step 3: Navigation events
            val navEvent1Response = client.submitForm(
                url = SapPortalUrls.getNavEvent1Url(),
                formParameters = Parameters.build {
                    SapPortalParams.getNavEvent1Params().forEach { (key, value) -> append(key, value) }
                }
            ) {
                header("content-type", "application/x-www-form-urlencoded")
                header("referer", SapPortalUrls.getLoginPageUrl())
                header("sec-fetch-dest", "iframe")
                header("sec-fetch-mode", "navigate")
                header("sec-fetch-site", "same-origin")
            }

            if (!navEvent1Response.status.isSuccess()) {
                return AttendanceResult.Error("Navigation Event 1 failed with status: ${navEvent1Response.status.value}")
            }

            val navEvent2Response = client.submitForm(
                url = SapPortalUrls.getNavEvent2Url(),
                formParameters = Parameters.build {
                    SapPortalParams.getNavEvent2Params().forEach { (key, value) -> append(key, value) }
                }
            ) {
                header("content-type", "application/x-www-form-urlencoded")
                header("referer", SapPortalUrls.getLoginPageUrl())
                header("sec-fetch-dest", "iframe")
                header("sec-fetch-mode", "navigate")
                header("sec-fetch-site", "same-origin")
            }

            if (!navEvent2Response.status.isSuccess()) {
                return AttendanceResult.Error("Navigation Event 2 failed with status: ${navEvent2Response.status.value}")
            }

            val navEvent3Response = client.submitForm(
                url = SapPortalUrls.getNavEvent3Url(),
                formParameters = Parameters.build {
                    SapPortalParams.getNavEvent3Params().forEach { (key, value) -> append(key, value) }
                }
            ) {
                header("content-type", "application/x-www-form-urlencoded")
                header("referer", SapPortalUrls.getLoginPageUrl())
                header("sec-fetch-dest", "iframe")
                header("sec-fetch-mode", "navigate")
                header("sec-fetch-site", "same-origin")
            }

            if (!navEvent3Response.status.isSuccess()) {
                return AttendanceResult.Error("Navigation Event 3 failed with status: ${navEvent3Response.status.value}")
            }

            val nav3Html = navEvent3Response.bodyAsText()

            // Step 4: Extract Web Dynpro form action
            val wdFormAction = SapPortalHtmlParser.extractWebDynproFormAction(nav3Html)

            
            if (wdFormAction.isNullOrEmpty()) {
                return AttendanceResult.Error("Failed to extract Web Dynpro form action")
            }

            val sapExtSid = SapPortalTokenExtractor.extractSapExtSid(wdFormAction)

            if (sapExtSid.isNullOrEmpty()) {
                return AttendanceResult.Error("Could not extract sap-ext-sid from form action. Action: $wdFormAction")
            }

            // Step 5: Submit Web Dynpro form
            val formData = SapPortalHtmlParser.extractFormFields(nav3Html)

            
            val wdInitialResponse = client.submitForm(
                url = wdFormAction,
                formParameters = Parameters.build {
                    formData.forEach { (key, value) -> append(key, value) }
                }
            ) {
                headers {
                    SapPortalHeaders.webDynproHeaders.forEach { (key, value) ->
                        set(key, value) // Use set to avoid duplicates from DefaultRequest
                    }
                }
            }

            if (!wdInitialResponse.status.isSuccess()) {
                return AttendanceResult.Error("Web Dynpro form submission failed with status: ${wdInitialResponse.status.value}")
            }

            val wdResponseHtml = wdInitialResponse.bodyAsText()
            val responseUrl = wdInitialResponse.call.request.url.toString()

            // Step 6: Extract tokens
            val wdContextId = SapPortalTokenExtractor.extractContextId(wdResponseHtml, responseUrl)
            val secureId = SapPortalTokenExtractor.extractSecureId(wdResponseHtml)


            val sapClientForm = Ksoup.parse(wdResponseHtml).selectFirst(SapPortalHeaders.sapClientFormSelector)
            val formAction = sapClientForm?.attr("action")

            val (extSidFromForm, contextIdFromForm) = SapPortalTokenExtractor.extractTokensFromFormAction(formAction)

            val finalExtSid = extSidFromForm ?: sapExtSid
            var finalContextId = contextIdFromForm ?: wdContextId

            if (finalContextId == wdContextId) {
                val urlContextIdMatch = Regex("""[?&]sap-contextid=([^&]+)""", RegexOption.IGNORE_CASE).find(responseUrl)
                if (urlContextIdMatch != null && urlContextIdMatch.groupValues.size > 1) {
                    val urlContextId = urlContextIdMatch.groupValues[1].decodeURLPart()
                    if (urlContextId.isNotEmpty()) {
                        finalContextId = urlContextId
                    }
                }
            }

            if (finalExtSid.isEmpty() || finalContextId.isEmpty() || secureId.isEmpty()) {
                return AttendanceResult.Error("Missing required tokens: ext-sid=${finalExtSid.isNotEmpty()}, context-id=${finalContextId.isNotEmpty()}, secure-id=${secureId.isNotEmpty()}")
            }

            // Step 7: Initial attendance load
            val initialUrl = SapPortalUrls.getInitialAttendanceUrl(finalExtSid, finalContextId)

            
            val initialBody = SapPortalParams.getInitialAttendanceBody(secureId, finalContextId)

            val initialResponse = client.submitForm(
                url = initialUrl,
                formParameters = Parameters.build {
                    initialBody.forEach { (key, value) -> append(key, value) }
                }
            ) {
                headers {
                    SapPortalHeaders.getInitialHeaders().forEach { (key, value) ->
                        set(key, value)
                    }
                }
            }

            if (!initialResponse.status.isSuccess()) {
                val responseContent = initialResponse.bodyAsText()
                return AttendanceResult.Error("Initial attendance load failed with status: ${initialResponse.status.value}. Response: ${responseContent.take(200)}")
            }
            
            val initialResponseBody = initialResponse.bodyAsText()
            
            // Step 8: Detect academic year and term
            // Use Step 7 response (delta) if it contains content, otherwise fallback to Step 5 (shell)
            var htmlToParse = wdResponseHtml
            val cdataMatch = SapPortalHeaders.contentUpdateRegex.find(initialResponseBody)
            if (cdataMatch != null) {
                htmlToParse = cdataMatch.groupValues[1]
            } else if (initialResponseBody.length > wdResponseHtml.length) {
                htmlToParse = initialResponseBody
            }

            val (academicYearValue, termCodeValue) = SapPortalHtmlParser.detectAcademicYearAndTerm(htmlToParse, academicYear, termCode)



            // Step 9: Request attendance with selection
            val attendanceBody = SapPortalParams.getAttendanceBodyWithSelection(secureId, academicYearValue, termCodeValue).toMutableMap()

            val encodedExtSid = finalExtSid.replace("*", "*").replace("-", "--").encodeURLQueryComponent()
            val sapeventQueue = attendanceBody["SAPEVENTQUEUE"]?.replace("PLACEHOLDER_EXT_SID", encodedExtSid) ?: ""
            attendanceBody["SAPEVENTQUEUE"] = sapeventQueue

            val attendanceResponse = client.submitForm(
                url = initialUrl,
                formParameters = Parameters.build {
                    attendanceBody.forEach { (key, value) -> append(key, value) }
                }
            ) {
                headers {
                    SapPortalHeaders.getInitialHeaders().forEach { (key, value) ->
                        append(key, value)
                    }
                }
            }

            if (!attendanceResponse.status.isSuccess()) {
                val responseContent = attendanceResponse.bodyAsText()
                return AttendanceResult.Error("Attendance request failed with status: ${attendanceResponse.status.value}. Response: ${responseContent.take(200)}")
            }

            val attendanceHtml = attendanceResponse.bodyAsText()

            // Step 10: Parse attendance data
            val parsedAttendance = AttendanceData(SapPortalHtmlParser.parseAttendanceData(attendanceHtml))

            performLogout(client)

            return AttendanceResult.Success(parsedAttendance)

        } catch (e: Exception) {
            try { performLogout(client) } catch(e: Exception) {}
            e.printStackTrace()
            val errorMessage = if (e.message?.contains("Unable to resolve host") == true) {
                val host = e.message?.substringAfter("Unable to resolve host ", "")?.substringBefore(":") ?: "unknown host"
                "Network error: Unable to connect to $host. The portal URL may have changed. Please verify that the portal URL is correct. Common causes: ICT Cell has changed the server URL or there are network connectivity issues."
            } else {
                "Network error: ${e.message ?: e::class.simpleName}"
            }
            AttendanceResult.Error(errorMessage)
        } finally {
             client.close()
        }
        return AttendanceResult.Error("Unexpected exit") // Fallback
    }


    private fun secureWipe(charArray: CharArray) {
        for (i in charArray.indices) {
            charArray[i] = '0'
        }
    }

    private suspend fun performLogout(client: HttpClient) {
        try {
            val logoutResponse = client.submitForm(
                url = SapPortalUrls.getLogoutUrl(),
                formParameters = Parameters.build {
                    append("logout_submit", "true")
                }
            ) {
                header("content-type", "application/x-www-form-urlencoded")
                header("origin", AppConfig.portalBase)
                header("referer", SapPortalUrls.getLoginPageUrl())
            }

            if (logoutResponse.status.isSuccess()) {

            } else {

            }
        } catch (e: Exception) {
            // Don't print error if it's a host resolution issue during logout
            // since the main fetch operation might have already failed due to network issues
            if (!e.message.toString().contains("Unable to resolve host")) {

            } else {

            }
        }
    }
}

sealed class AttendanceResult {
    data class Success(val data: AttendanceData) : AttendanceResult()
    data class Error(val message: String) : AttendanceResult()
}

data class AttendanceData(
    val subjects: List<SubjectAttendance> = emptyList()
)

data class SubjectAttendance(
    val subjectCode: String,
    val subjectName: String,
    val attendedClasses: Int,
    val totalClasses: Int,
    val percentage: Double,
    val facultyName: String = ""
)

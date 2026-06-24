package com.kito.sap.sensitive

object SapPortalTokenExtractor {
    fun extractSapExtSid(formAction: String): String? = null
    fun extractContextId(wdResponseHtml: String?, responseUrl: String?): String = ""
    fun extractSecureId(wdResponseHtml: String?): String = ""
    fun extractTokensFromFormAction(formAction: String?): Pair<String?, String?> = Pair(null, null)
}

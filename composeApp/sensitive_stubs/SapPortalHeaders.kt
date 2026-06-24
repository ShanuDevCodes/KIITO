package com.kito.sap.sensitive

object SapPortalHeaders {
    fun getInitialHeaders(): Map<String, String> = emptyMap()
    val webDynproHeaders = emptyMap<String, String>()
    val sapClientFormSelector = "form"
    val contentUpdateRegex = Regex("")
    
    // Selectors and regexes to match the real implementation
    val sapExtSidRegex = Regex("")
    val contextIdPatterns = emptyList<Regex>()
    val workAreaFormSelector = ""
    val yearPopupListSelector = ""
    val yearInputSelector = ""
    val sessionPopupListSelector = ""
    val sessionInputSelector = ""
    val submitButtonSelector = ""
    val rowRegex = Regex("")
}

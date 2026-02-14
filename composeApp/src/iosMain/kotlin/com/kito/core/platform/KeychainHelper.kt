package com.kito.core.platform

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.value
import platform.CoreFoundation.CFDictionaryCreateMutable
import platform.CoreFoundation.CFDictionaryRef
import platform.CoreFoundation.CFDictionarySetValue
import platform.CoreFoundation.CFRelease
import platform.CoreFoundation.CFStringCreateWithCString
import platform.CoreFoundation.CFTypeRefVar
import platform.CoreFoundation.kCFAllocatorDefault
import platform.CoreFoundation.kCFBooleanTrue
import platform.CoreFoundation.kCFTypeDictionaryKeyCallBacks
import platform.CoreFoundation.kCFTypeDictionaryValueCallBacks
import platform.Foundation.CFBridgingRelease
import platform.Foundation.CFBridgingRetain
import platform.Foundation.NSData
import platform.Foundation.NSString
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.create
import platform.Foundation.dataUsingEncoding
import platform.Security.SecItemAdd
import platform.Security.SecItemCopyMatching
import platform.Security.SecItemDelete
import platform.Security.SecItemUpdate
import platform.Security.kSecAttrAccount
import platform.Security.kSecAttrService
import platform.Security.kSecClass
import platform.Security.kSecClassGenericPassword
import platform.Security.kSecMatchLimit
import platform.Security.kSecMatchLimitOne
import platform.Security.kSecReturnData
import platform.Security.kSecValueData
import platform.Security.errSecSuccess
import platform.Security.errSecDuplicateItem

@OptIn(ExperimentalForeignApi::class)
object KeychainHelper {

    fun save(service: String, account: String, data: String) {
        val nsData = NSString.create(string = data).dataUsingEncoding(NSUTF8StringEncoding)
        
        val query = CFDictionaryCreateMutable(
            kCFAllocatorDefault,
            4,
            kCFTypeDictionaryKeyCallBacks.ptr,
            kCFTypeDictionaryValueCallBacks.ptr
        )
        
        CFDictionarySetValue(query, kSecClass, kSecClassGenericPassword)
        CFDictionarySetValue(query, kSecAttrService, CFStringCreateWithCString(null, service, platform.CoreFoundation.kCFStringEncodingUTF8.toUInt()))
        CFDictionarySetValue(query, kSecAttrAccount, CFStringCreateWithCString(null, account, platform.CoreFoundation.kCFStringEncodingUTF8.toUInt()))
        CFDictionarySetValue(query, kSecValueData, CFBridgingRetain(nsData))

        try {
            val status = SecItemAdd(query, null)

            if (status == errSecDuplicateItem) {

                
                val updateQuery = CFDictionaryCreateMutable(
                    kCFAllocatorDefault,
                    3,
                    kCFTypeDictionaryKeyCallBacks.ptr,
                    kCFTypeDictionaryValueCallBacks.ptr
                )
                
                CFDictionarySetValue(updateQuery, kSecClass, kSecClassGenericPassword)
                CFDictionarySetValue(updateQuery, kSecAttrService, CFStringCreateWithCString(null, service, platform.CoreFoundation.kCFStringEncodingUTF8.toUInt()))
                CFDictionarySetValue(updateQuery, kSecAttrAccount, CFStringCreateWithCString(null, account, platform.CoreFoundation.kCFStringEncodingUTF8.toUInt()))

                val attributesToUpdate = CFDictionaryCreateMutable(
                    kCFAllocatorDefault,
                    1,
                    kCFTypeDictionaryKeyCallBacks.ptr,
                    kCFTypeDictionaryValueCallBacks.ptr
                )
                CFDictionarySetValue(attributesToUpdate, kSecValueData, CFBridgingRetain(nsData))

                try {
                    val updateStatus = SecItemUpdate(updateQuery, attributesToUpdate)

                } finally {
                    if (updateQuery != null) CFRelease(updateQuery)
                    if (attributesToUpdate != null) CFRelease(attributesToUpdate)
                }
            } else {

            }
        } finally {
            if (query != null) CFRelease(query)
        }
    }

    fun read(service: String, account: String): String? {
        val query = CFDictionaryCreateMutable(
            kCFAllocatorDefault,
            5,
            kCFTypeDictionaryKeyCallBacks.ptr,
            kCFTypeDictionaryValueCallBacks.ptr
        )
        
        CFDictionarySetValue(query, kSecClass, kSecClassGenericPassword)
        CFDictionarySetValue(query, kSecAttrService, CFStringCreateWithCString(null, service, platform.CoreFoundation.kCFStringEncodingUTF8.toUInt()))
        CFDictionarySetValue(query, kSecAttrAccount, CFStringCreateWithCString(null, account, platform.CoreFoundation.kCFStringEncodingUTF8.toUInt()))
        CFDictionarySetValue(query, kSecReturnData, kCFBooleanTrue)
        CFDictionarySetValue(query, kSecMatchLimit, kSecMatchLimitOne)

        try {
            memScoped {
                val result = alloc<CFTypeRefVar>()
                val status = SecItemCopyMatching(query, result.ptr)
                
                if (status == errSecSuccess) {
                    val data = CFBridgingRelease(result.value) as? NSData
                    return data?.let {
                        val string = NSString.create(data = it, encoding = NSUTF8StringEncoding)
                        string.toString()
                    }
                } else {

                }
            }
        } finally {
            if (query != null) CFRelease(query)
        }
        return null
    }

    fun delete(service: String, account: String) {
        val query = CFDictionaryCreateMutable(
            kCFAllocatorDefault,
            3,
            kCFTypeDictionaryKeyCallBacks.ptr,
            kCFTypeDictionaryValueCallBacks.ptr
        )
        
        CFDictionarySetValue(query, kSecClass, kSecClassGenericPassword)
        CFDictionarySetValue(query, kSecAttrService, CFStringCreateWithCString(null, service, platform.CoreFoundation.kCFStringEncodingUTF8.toUInt()))
        CFDictionarySetValue(query, kSecAttrAccount, CFStringCreateWithCString(null, account, platform.CoreFoundation.kCFStringEncodingUTF8.toUInt()))

        try {
            SecItemDelete(query)
        } finally {
            if (query != null) CFRelease(query)
        }
    }
}

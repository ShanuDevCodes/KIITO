package com.kito.core.platform

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.crypto.tink.Aead
import com.google.crypto.tink.KeyTemplates
import com.google.crypto.tink.aead.AeadConfig
import com.google.crypto.tink.integration.android.AndroidKeysetManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlin.jvm.Throws

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "encrypted_data_store")

actual class SecureStorage(private val context: Context) {

    private companion object {
        private val KEY_SAP_PASSWORD = stringPreferencesKey("sap_password")
        private val KEY_LOGGED_IN = booleanPreferencesKey("logged_in")
        const val KEYSET_NAME = "kito_master_keyset"
        const val PREFERENCE_FILE = "__androidx_security_crypto_encrypted_prefs__"
    }

    private val aEAD: Aead? by lazy {
        try {
            AeadConfig.register()
            val keysetHandle = AndroidKeysetManager.Builder()
                .withSharedPref(context, KEYSET_NAME, PREFERENCE_FILE)
                .withKeyTemplate(KeyTemplates.get("AES256_GCM"))
                .withMasterKeyUri("android-keystore://kito_master_key")
                .build()
                .keysetHandle
            keysetHandle.getPrimitive(Aead::class.java)
        } catch (_: Exception) {
            null
        }
    }


    private fun encrypt(plainText: String): String {
        val aEAD = this.aEAD ?: throw Exception("No key found")
        val cipherText = aEAD.encrypt(plainText.toByteArray(Charsets.UTF_8), null)
        return android.util.Base64.encodeToString(cipherText, android.util.Base64.DEFAULT)
    }

    fun decrypt(cipherTextBase64: String): String {
        val aEAD = this.aEAD ?: throw Exception("No key found")
        val cipherText =
            android.util.Base64.decode(cipherTextBase64, android.util.Base64.DEFAULT)
        val plainText = aEAD.decrypt(cipherText, null)
        return String(plainText, Charsets.UTF_8)
    }

    actual suspend fun saveSapPassword(password: String): Boolean {
        val encryptedPassword = encrypt(password)
        context.dataStore.edit { prefs ->
            prefs[KEY_SAP_PASSWORD] = encryptedPassword
            prefs[KEY_LOGGED_IN] = true
        }
        return true
    }


    actual suspend fun getSapPassword(): String {
        try {
            val encrypted = context.dataStore.data.first()[KEY_SAP_PASSWORD]
            return if (encrypted.isNullOrEmpty()) {
                ""
            } else {
                decrypt(encrypted)
            }
        } catch (_: Exception) {
            clearSapPassword()
            return ""
        }
    }

    actual val isLoggedInFlow: Flow<Boolean> = context.dataStore.data
        .map { prefs ->
            prefs[KEY_LOGGED_IN] ?: false
        }

    actual suspend fun clearSapPassword(): Boolean {
        context.dataStore.edit { prefs ->
            prefs.remove(KEY_SAP_PASSWORD)
            prefs[KEY_LOGGED_IN] = false
        }
        return true
    }
}

//import android.content.Context
//import android.content.SharedPreferences
//import androidx.security.crypto.EncryptedSharedPreferences
//import androidx.security.crypto.MasterKey
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.channels.awaitClose
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.flow.callbackFlow
//import kotlinx.coroutines.withContext
//
//actual class SecureStorage(context: Context) {
//    private companion object {
//        const val KEY_SAP_PASSWORD = "sap_password"
//        const val KEY_LOGGED_IN = "logged_in"
//    }
//
//    private val masterKey = MasterKey.Builder(context)
//        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
//        .build()
//
//    private val prefs = EncryptedSharedPreferences.create(
//        context,
//        "secure_prefs",
//        masterKey,
//        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
//        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
//    )
//
//    actual suspend fun saveSapPassword(password: String): Boolean =
//        withContext(Dispatchers.IO) {
//            prefs.edit()
//                .putString(KEY_SAP_PASSWORD, password)
//                .putBoolean(KEY_LOGGED_IN, true)
//                .commit()
//        }
//
//    actual suspend fun getSapPassword(): String =
//        withContext(Dispatchers.IO) {
//            prefs.getString(KEY_SAP_PASSWORD, "") ?: ""
//        }
//
//    actual val isLoggedInFlow: Flow<Boolean> = callbackFlow {
//        val sendValue = {
//            trySend(prefs.getBoolean(KEY_LOGGED_IN, false))
//        }
//
//        sendValue()
//
//        val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
//            if (key == KEY_LOGGED_IN) {
//                sendValue()
//            }
//        }
//
//        prefs.registerOnSharedPreferenceChangeListener(listener)
//        awaitClose { prefs.unregisterOnSharedPreferenceChangeListener(listener) }
//    }
//
//    actual suspend fun clearSapPassword(): Boolean =
//        withContext(Dispatchers.IO) {
//            prefs.edit()
//                .remove(KEY_SAP_PASSWORD)
//                .putBoolean(KEY_LOGGED_IN, false)
//                .commit()
//        }
//}
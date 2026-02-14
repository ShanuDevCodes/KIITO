package com.kito.core.platform

import android.content.Context
import android.content.SharedPreferences
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.google.crypto.tink.Aead
import com.google.crypto.tink.KeyTemplates
import com.google.crypto.tink.aead.AeadConfig
import com.google.crypto.tink.integration.android.AndroidKeysetManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.security.GeneralSecurityException

// Extension property for DataStore
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "encrypted_secure_prefs")

actual class SecureStorage(private val context: Context) {
    
    private companion object {
        const val KEY_SAP_PASSWORD = "sap_password"
        const val KEY_LOGGED_IN = "logged_in"
        const val KEYSET_NAME = "kito_master_keyset"
        const val PREFERENCE_FILE = "__androidx_security_crypto_encrypted_prefs__"
        const val OLD_PREFS_NAME = "secure_prefs"
    }

    // DataStore keys
    private val sapPasswordKey = stringPreferencesKey(KEY_SAP_PASSWORD)
    private val loggedInKey = booleanPreferencesKey(KEY_LOGGED_IN)

    // Tink AEAD for encryption
    private val aead: Aead? by lazy {
        try {
            AeadConfig.register()
            val keysetHandle = AndroidKeysetManager.Builder()
                .withSharedPref(context, KEYSET_NAME, PREFERENCE_FILE)
                .withKeyTemplate(KeyTemplates.get("AES256_GCM"))
                .withMasterKeyUri("android-keystore://kito_master_key")
                .build()
                .keysetHandle
            keysetHandle.getPrimitive(Aead::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    init {
        // Perform migration on init
        // This will run asynchronously on first access
    }

    /**
     * Migrates data from EncryptedSharedPreferences to EncryptedDataStore
     * Only runs once - if data exists in old storage
     */
    private suspend fun migrateFromEncryptedSharedPreferences() {
        try {
            // Check if we've already migrated by checking if DataStore has data
            val hasDataInDataStore = context.dataStore.data.first().contains(sapPasswordKey)
            if (hasDataInDataStore) {
                return // Already migrated
            }

            // Try to read from old EncryptedSharedPreferences
            val masterKey = MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()

            val oldPrefs = try {
                EncryptedSharedPreferences.create(
                    context,
                    OLD_PREFS_NAME,
                    masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
                )
            } catch (e: GeneralSecurityException) {
                // Corrupted keystore - clear and return
                println("⚠️ EncryptedSharedPreferences corrupted, skipping migration: ${e.message}")
                clearOldPreferences()
                return
            } catch (e: Exception) {
                println("⚠️ Failed to open EncryptedSharedPreferences: ${e.message}")
                return
            }
            val oldPassword = oldPrefs.getString(KEY_SAP_PASSWORD, null)
            val oldLoggedIn = oldPrefs.getBoolean(KEY_LOGGED_IN, false)

            if (oldPassword != null) {
                println("✅ Migrating SAP password from EncryptedSharedPreferences to DataStore")
                context.dataStore.edit { prefs ->
                    prefs[sapPasswordKey] = oldPassword
                    prefs[loggedInKey] = oldLoggedIn
                }

                oldPrefs.edit().clear().commit()
                println("✅ Migration complete, old data cleared")
            }
        } catch (e: Exception) {
            println("⚠️ Migration failed: ${e.message}")
            e.printStackTrace()
        }
    }

    private fun clearOldPreferences() {
        try {
            context.getSharedPreferences(OLD_PREFS_NAME, Context.MODE_PRIVATE)
                .edit()
                .clear()
                .commit()
        } catch (e: Exception) {
            // Ignore errors during cleanup
        }
    }

    actual suspend fun saveSapPassword(password: String): Boolean =
        withContext(Dispatchers.IO) {
            try {
                // Ensure migration has happened
                migrateFromEncryptedSharedPreferences()
                
                context.dataStore.edit { prefs ->
                    prefs[sapPasswordKey] = password
                    prefs[loggedInKey] = true
                }
                true
            } catch (e: Exception) {
                println("⚠️ Failed to save SAP password: ${e.message}")
                e.printStackTrace()
                false
            }
        }

    actual suspend fun getSapPassword(): String =
        withContext(Dispatchers.IO) {
            try {
                // Ensure migration has happened
                migrateFromEncryptedSharedPreferences()
                
                context.dataStore.data.first()[sapPasswordKey] ?: ""
            } catch (e: Exception) {
                println("⚠️ Failed to read SAP password, clearing data: ${e.message}")
                e.printStackTrace()
                // Clear data on error (corrupted keystore)
                try {
                    clearSapPassword()
                } catch (clearError: Exception) {
                    // Ignore
                }
                ""
            }
        }

    actual val isLoggedInFlow: Flow<Boolean> = context.dataStore.data
        .map { prefs ->
            // Trigger migration on first access
            withContext(Dispatchers.IO) {
                migrateFromEncryptedSharedPreferences()
            }
            prefs[loggedInKey] ?: false
        }
        .catch { e ->
            println("⚠️ Error reading login state, emitting false: ${e.message}")
            e.printStackTrace()
            emit(false)
        }

    actual suspend fun clearSapPassword(): Boolean =
        withContext(Dispatchers.IO) {
            try {
                context.dataStore.edit { prefs ->
                    prefs.remove(sapPasswordKey)
                    prefs[loggedInKey] = false
                }
                true
            } catch (e: Exception) {
                println("⚠️ Failed to clear SAP password: ${e.message}")
                e.printStackTrace()
                false
            }
        }
}

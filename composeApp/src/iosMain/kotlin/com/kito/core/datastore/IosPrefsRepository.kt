package com.kito.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class IosPrefsRepository(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        private val KEY_IOS_IS_LOGGED_IN = booleanPreferencesKey("ios_is_logged_in")
    }

    val isLoggedInFlow: Flow<Boolean> = dataStore.data
        .map { it[KEY_IOS_IS_LOGGED_IN] ?: false }

    suspend fun setLoggedIn(isLoggedIn: Boolean) {
        dataStore.edit {
            it[KEY_IOS_IS_LOGGED_IN] = isLoggedIn
        }
    }
}

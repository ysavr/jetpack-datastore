package com.savr.datastore

import android.content.Context
import android.preference.Preference
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.IOException

// At the top level of your kotlin file:
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "store_preference")

enum class UiMode {
    LIGHT, DARK
}

object DataStoreManager {

    suspend fun saveValue(key: String, value: String, dataStore: DataStore<Preferences>) {
        val storeKey = stringPreferencesKey(key)
        dataStore.edit {
            it[storeKey] = value
        }
    }

    suspend fun saveValue(key: String, value: Int, dataStore: DataStore<Preferences>) {
        val storeKey = intPreferencesKey(key)
        dataStore.edit {
            it[storeKey] = value
        }
    }

    suspend fun saveValue(key: String, value: Double, dataStore: DataStore<Preferences>) {
        val storeKey = doublePreferencesKey(key)
        dataStore.edit {
            it[storeKey] = value
        }
    }

    suspend fun saveValue(key: String, value: Long, dataStore: DataStore<Preferences>) {
        val storeKey = longPreferencesKey(key)
        dataStore.edit {
            it[storeKey] = value
        }
    }

    suspend fun saveValue(key: String, value: Boolean, dataStore: DataStore<Preferences>) {
        val storeKey = booleanPreferencesKey(key)
        dataStore.edit {
            it[storeKey] = value
        }
    }

    suspend fun getStringValue(dataStore: DataStore<Preferences>, key: String, default: String = ""): String {
        val storeKey = stringPreferencesKey(key)
        val valueFlow: Flow<String> = dataStore.data.map {
            it[storeKey] ?: default
        }
        return valueFlow.first()
    }

    suspend fun getIntValue(context: Context, key: String, default: Int = 0): Int {
        val storeKey = intPreferencesKey(key)
        val valueFlow: Flow<Int> = context.dataStore.data.map {
            it[storeKey] ?: default
        }
        return valueFlow.first()
    }

    suspend fun getDoubleValue(context: Context, key: String, default: Double = 0.0): Double {
        val storeKey = doublePreferencesKey(key)
        val valueFlow: Flow<Double> = context.dataStore.data.map {
            it[storeKey] ?: default
        }
        return valueFlow.first()
    }

    suspend fun getLongValue(context: Context, key: String, default: Long = 0L): Long {
        val storeKey = longPreferencesKey(key)
        val valueFlow: Flow<Long> = context.dataStore.data.map {
            it[storeKey] ?: default
        }
        return valueFlow.first()
    }

    suspend fun getBooleanValue(context: Context, key: String, default: Boolean = false): Boolean {
        val storeKey = booleanPreferencesKey(key)
        val valueFlow: Flow<Boolean> = context.dataStore.data.map {
            it[storeKey] ?: default
        }
        return valueFlow.first()
    }

    fun getBoolean(dataStore: DataStore<Preferences>, key: String): Flow<Boolean> {
        return dataStore.data.map {
            it[booleanPreferencesKey(key)] ?: false
        }
    }

    suspend fun removeData(dataStore: DataStore<Preferences>) {
        dataStore.edit {
            it.clear()
        }
    }

    suspend fun setUiMode(dataStore: DataStore<Preferences>, uiMode: UiMode) {
        dataStore.edit {
            it[booleanPreferencesKey("dark_mode")] = when(uiMode) {
                UiMode.LIGHT -> false
                UiMode.DARK -> true
            }
        }
    }

    fun uiModeFlow(dataStore: DataStore<Preferences>): Flow<UiMode> {
        return dataStore.data.catch {
            if (it is IOException) {
                it.printStackTrace()
                emit(emptyPreferences())
            } else {
                throw it
            }
        }.map {
            when(it[booleanPreferencesKey("dark_mode")] ?: false) {
                true -> UiMode.DARK
                false -> UiMode.LIGHT
            }
        }
    }
}
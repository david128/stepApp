package com.example.android.stepapp.pref

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.IOException

val Context.dataStore: DataStore<Preferences> by preferencesDataStore (name= "settings")
class Pref(context: Context) {

    private val dataStore = context.dataStore

    private object PreferenceKeys {
        val historicalMode = booleanPreferencesKey("historical_mode")
        val goalEditingMode = booleanPreferencesKey("goal_editing_mode")
    }


    suspend fun saveHistoricalToDS(boolean: Boolean) {
        dataStore.edit { preference ->
            preference[PreferenceKeys.historicalMode] = boolean

        }
    }

    suspend fun saveGoalEditingToDS(boolean: Boolean) {
        dataStore.edit { preference ->
            preference[PreferenceKeys.goalEditingMode] = boolean

        }
    }


    val readHistoricalFromDS: Flow<Boolean> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                Log.d("DataStore", exception.message.toString())
            } else {
                throw exception
            }
        }
        .map { preference ->
            val isEnabled = preference[PreferenceKeys.historicalMode] ?: false
            isEnabled
        }

    val readGoalEditingFromDS: Flow<Boolean> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                Log.d("DataStore", exception.message.toString())
            } else {
                throw exception
            }
        }
        .map { preference ->
            val isEnabled = preference[PreferenceKeys.goalEditingMode] ?: false
            isEnabled
        }


}
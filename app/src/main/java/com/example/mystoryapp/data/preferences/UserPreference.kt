package com.example.mystoryapp.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.mystoryapp.data.api.responses.LoginResult
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.Flow

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "UserPreference")

class UserPreference constructor(context: Context){
    private val dataStore = context.dataStore

    fun getUserPreference(): Flow<LoginResult?>{
        return dataStore.data.map {preferences ->
            if (preferences[TOKEN_KEY] != null){
                LoginResult(
                    preferences[USERID_KEY] ?: "",
                    preferences[NAME_KEY] ?: "",
                    preferences[TOKEN_KEY] ?: ""
                )
            } else {
                null
            }
        }
    }

    fun getUserToken(): Flow<String?> {
        return dataStore.data.map {preferences ->
            preferences[TOKEN_KEY]
        }
    }

    suspend fun saveUserPreference(loginResult: LoginResult){
        dataStore.edit { mutablePreferences ->
            mutablePreferences[USERID_KEY] = loginResult.userId
            mutablePreferences[NAME_KEY] = loginResult.name
            mutablePreferences[TOKEN_KEY] = loginResult.token
        }
    }

    suspend fun logoutUser() {
        dataStore.edit {mutablePreferences ->
            mutablePreferences.remove(USERID_KEY)
            mutablePreferences.remove(NAME_KEY)
            mutablePreferences.remove(TOKEN_KEY)
        }
    }

    companion object {
        private val USERID_KEY = stringPreferencesKey("userId")
        private val NAME_KEY = stringPreferencesKey("name")
        private val TOKEN_KEY = stringPreferencesKey("token")
    }
}
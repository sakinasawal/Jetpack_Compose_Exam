package io.rapidz.assignment1.storage

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object DataStoreManager {
	private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "candidate_prefs")

	private val CANDIDATE_NAME = stringPreferencesKey("candidate_name")
	private val CANDIDATE_EMAIL = stringPreferencesKey("candidate_email")
	private val TEST_TIME_LIMIT = intPreferencesKey("test_time_limit")

	suspend fun saveCandidateData(context: Context, name: String, email: String) {
		context.dataStore.edit { preferences ->
			preferences[CANDIDATE_NAME] = name
			preferences[CANDIDATE_EMAIL] = email
		}
	}

	fun getCandidateName(context: Context): Flow<String?> {
		return context.dataStore.data.map { preferences ->
			preferences[CANDIDATE_NAME]
		}
	}

	fun getCandidateEmail(context: Context): Flow<String?> {
		return context.dataStore.data.map { preferences ->
			preferences[CANDIDATE_EMAIL]
		}
	}

	suspend fun saveTestTimeLimit(context: Context, timeLimit: Int) {
		context.dataStore.edit { preferences ->
			preferences[TEST_TIME_LIMIT] = timeLimit
		}
	}

	fun getTestTimeLimit(context: Context): Flow<Int> {
		return context.dataStore.data.map { preferences ->
			preferences[TEST_TIME_LIMIT] ?: 0
		}
	}
}
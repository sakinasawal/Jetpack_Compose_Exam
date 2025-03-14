package io.rapidz.assignment1.storage

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = DataStoreManager.DATA_STORE)

class DataStoreManager @Inject constructor(@ApplicationContext private val context: Context) : DataStoreInterface {

	companion object {
		const val DATA_STORE = "DATA_STORE"
		val CANDIDATE_NAME = stringPreferencesKey("CANDIDATE_NAME")
		val CANDIDATE_EMAIL = stringPreferencesKey("CANDIDATE_EMAIL")
	}

	override suspend fun <T> writeToDataStore(key: Preferences.Key<T>, value: T) {
		context.dataStore.edit { preferences ->
			preferences[key] = value
		}
	}

	override suspend fun writeMultipleToDataStore(vararg pairs: DataStoreValue<*>) {
		context.dataStore.edit { preferences ->
			for (pair in pairs) {
				when (pair) {
					is DataStoreValue.StringValue -> preferences[pair.key] = pair.value
					is DataStoreValue.IntValue -> preferences[pair.key] = pair.value
					is DataStoreValue.LongValue -> preferences[pair.key] = pair.value
					is DataStoreValue.BooleanValue -> preferences[pair.key] = pair.value
					is DataStoreValue.FloatValue -> preferences[pair.key] = pair.value
				}
			}
		}
	}

	override suspend fun <T> readFromDataStore(key: Preferences.Key<T>): T? {
		return context.dataStore.data.first()[key]
	}

	private val CANDIDATE_NAME = stringPreferencesKey("candidate_name")
	private val CANDIDATE_EMAIL = stringPreferencesKey("candidate_email")
	private val TEST_TIME_LIMIT = intPreferencesKey("test_time_limit")

//	suspend fun saveCandidateData(context: Context, name: String, email: String) {
//		context.dataStore.edit { preferences ->
//			preferences[CANDIDATE_NAME] = name
//			preferences[CANDIDATE_EMAIL] = email
//		}
//	}
//
//	fun getCandidateName(context: Context): Flow<String?> {
//		return context.dataStore.data.map { preferences ->
//			preferences[CANDIDATE_NAME]
//		}
//	}
//
//	fun getCandidateEmail(context: Context): Flow<String?> {
//		return context.dataStore.data.map { preferences ->
//			preferences[CANDIDATE_EMAIL]
//		}
//	}
//
//	suspend fun saveTestTimeLimit(context: Context, timeLimit: Int) {
//		context.dataStore.edit { preferences ->
//			preferences[TEST_TIME_LIMIT] = timeLimit
//		}
//	}
//
//	fun getTestTimeLimit(context: Context): Flow<Int> {
//		return context.dataStore.data.map { preferences ->
//			preferences[TEST_TIME_LIMIT] ?: 0
//		}
//	}
}
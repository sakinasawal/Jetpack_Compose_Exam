package io.rapidz.assignment1.storage

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import io.rapidz.assignment1.repository.CandidateRepository
import io.rapidz.assignment1.viewmodel.CandidateViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

object DataStoreManager {
	private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "candidate_prefs")

	private val CANDIDATE_NAME = stringPreferencesKey("candidate_name")
	private val CANDIDATE_EMAIL = stringPreferencesKey("candidate_email")

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
}
package io.rapidz.assignment1.viewmodel

import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.rapidz.assignment1.data.Candidate
import io.rapidz.assignment1.data.CandidateUiState
import io.rapidz.assignment1.data.QuestionData
import io.rapidz.assignment1.repository.Repository
import io.rapidz.assignment1.storage.DataStoreInterface
import io.rapidz.assignment1.storage.DataStoreManager
import io.rapidz.assignment1.storage.DataStoreValue
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CandidateViewModel @Inject constructor (
	private val repository : Repository,
	private val dataStore: DataStoreInterface
) : ViewModel() {

	private val candidateUiState = MutableStateFlow(CandidateUiState())
	val uiState: StateFlow<CandidateUiState> = candidateUiState

	private val _toastMessage = MutableStateFlow<String?>(null)
	val toastMessage: StateFlow<String?> = _toastMessage

	private val totalQuestions = QuestionData.question.size

	init {
		viewModelScope.launch {
			val name = dataStore.readFromDataStore(DataStoreManager.CANDIDATE_NAME) ?: ""
			candidateUiState.update { it.copy(name = name) }
		}
		viewModelScope.launch {
			val email = dataStore.readFromDataStore(DataStoreManager.CANDIDATE_EMAIL) ?: ""
			candidateUiState.update { it.copy(email = email) }
		}
	}

	fun onNameChange(newName : String){
		candidateUiState.update { it.copy(name=newName) }
	}

	fun onEmailChange(newEmail : String){
		candidateUiState.update { it.copy(email=newEmail) }
	}

	fun registerCandidate(onNavigate:(Long, Boolean)-> Unit){
		val name = candidateUiState.value.name
		val email = candidateUiState.value.email
		if (name.isBlank() || email.isBlank()) return

		viewModelScope.launch {

			val existingCandidate = repository.getCandidateByEmail(email)
			if (existingCandidate != null) {
				val candidateId = existingCandidate.id
				val answers = repository.getAnswersByCandidate(candidateId).first()

				val hasSavedAnswers = answers.isNotEmpty() && answers.any { it.answerText.isNotEmpty() }
				val hasCompletedTest = answers.size >= totalQuestions && answers.all { it.answerText.isNotEmpty() }

				if (hasCompletedTest) {
					// Show toast message and exit (no dialog)
					_toastMessage.value = "You have already completed the test." // Update state
					return@launch
				}

				if (hasSavedAnswers) {
					candidateUiState.update { it.copy(showDialog = true, candidateId = candidateId) }
				} else {
					onNavigate(candidateId, false)
				}

			} else {
				// Ensure new candidate ID is properly stored
				val newCandidate = Candidate(name = name, emailAddress = email)
				val candidateId = repository.insertCandidate(newCandidate)

				dataStore.writeMultipleToDataStore(
					DataStoreValue.StringValue(DataStoreManager.CANDIDATE_NAME, name),
					DataStoreValue.StringValue(DataStoreManager.CANDIDATE_EMAIL, email)
				)

				onNavigate(candidateId, false)
			}
		}
	}

	fun onContinueTest(){
		candidateUiState.value = candidateUiState.value.copy(showDialog = false)
	}

	fun onStartNewTest(){
		viewModelScope.launch {
			repository.deleteAnswersForCandidate(candidateUiState.value.candidateId ?: return@launch)
			candidateUiState.value = candidateUiState.value.copy(showDialog = false)
		}
	}

	fun clearToastMessage() {
		_toastMessage.value = null // Reset the toast message
	}
}
package io.rapidz.assignment1.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.rapidz.assignment1.data.Candidate
import io.rapidz.assignment1.data.CandidateUiState
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
			repository.getCandidateByEmail(email)?.let { existingCandidate ->
				val candidateId = existingCandidate.id
				val answer = repository.getAnswersByCandidate(candidateId).first()
				val usePreviousData = answer.isNotEmpty() && answer.any{it.answerText.isNotEmpty()}

				if (usePreviousData){
					candidateUiState.value = uiState.value.copy(showDialog = true, candidateId = candidateId)
				} else {
					onNavigate(candidateId, false)
				}
			} ?: run {
				val candidateId = repository.insertCandidate(Candidate(name=name, emailAddress = email))
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
}
package io.rapidz.assignment1.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.rapidz.assignment1.data.Candidate
import io.rapidz.assignment1.repository.CandidateRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CandidateViewModel @Inject constructor (
	private val candidateRepository : CandidateRepository
) : ViewModel() {

	fun insert(candidate : Candidate){
		viewModelScope.launch {
			candidateRepository.insertCandidate(candidate)
		}
	}

}

class CandidateViewModelFactory(private val repository: CandidateRepository) : ViewModelProvider.Factory {
	override fun <T : ViewModel> create(modelClass: Class<T>): T {
		if (modelClass.isAssignableFrom(CandidateViewModel::class.java)) {
			@Suppress("UNCHECKED_CAST")
			return CandidateViewModel(repository) as T
		}
		throw IllegalArgumentException("Unknown ViewModel class")
	}
}
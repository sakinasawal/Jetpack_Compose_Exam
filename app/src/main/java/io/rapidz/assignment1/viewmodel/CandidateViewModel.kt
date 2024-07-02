package io.rapidz.assignment1.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.rapidz.assignment1.data.Answer
import io.rapidz.assignment1.data.Candidate
import io.rapidz.assignment1.repository.CandidateRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CandidateViewModel @Inject constructor (
	private val candidateRepository : CandidateRepository
) : ViewModel() {

	private val _answers = mutableStateListOf<Answer>()
	val answers: List<Answer> = _answers

	private var _candidateId: Long? = null
	val candidateId: Long? get() = _candidateId

	fun setCandidateId(id: Long) {
		_candidateId = id
	}

	fun saveAnswer(questionIndex: Int, answer: String) {
		_candidateId?.let { id ->
			val newAnswer = Answer(candidateId = id, questionIndex = questionIndex, answer = answer)
			viewModelScope.launch {
				candidateRepository.saveAnswer(newAnswer)
			}
			_answers.removeAll { it.questionIndex == questionIndex }
			_answers.add(newAnswer)
		}
	}

	fun loadAnswers(candidateId: Long) {
		viewModelScope.launch {
			val loadedAnswers = candidateRepository.getAnswersForCandidate(candidateId)
			_answers.clear()
			_answers.addAll(loadedAnswers)
		}
	}

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
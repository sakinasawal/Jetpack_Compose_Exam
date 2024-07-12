package io.rapidz.assignment1.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.rapidz.assignment1.data.Answer
import io.rapidz.assignment1.data.Candidate
import io.rapidz.assignment1.repository.AnswerRepository
import io.rapidz.assignment1.repository.CandidateRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CandidateViewModel @Inject constructor (
	private val candidateRepository : CandidateRepository,
	private val answerRepository: AnswerRepository
) : ViewModel() {

	private val _answers = mutableStateListOf<Answer>()
	val answers: List<Answer> = _answers

	private var _candidateId: Long? = null

	fun insert(candidate : Candidate){
		viewModelScope.launch {
			candidateRepository.insertCandidate(candidate)
		}
	}

	fun saveAnswer(questionIndex: Int, answer: String) {
		_candidateId?.let { id ->
			val newAnswer = Answer(candidateId = id, questionIndex = questionIndex, answer = answer)
			viewModelScope.launch {
				answerRepository.saveAnswer(newAnswer)
			}
			_answers.removeAll { it.questionIndex == questionIndex }
			_answers.add(newAnswer)
		}
	}

}

class CandidateViewModelFactory(private val candidateRepository: CandidateRepository, private val answerRepository: AnswerRepository) : ViewModelProvider.Factory {
	override fun <T : ViewModel> create(modelClass: Class<T>): T {
		if (modelClass.isAssignableFrom(CandidateViewModel::class.java)) {
			@Suppress("UNCHECKED_CAST")
			return CandidateViewModel(candidateRepository, answerRepository) as T
		}
		throw IllegalArgumentException("Unknown ViewModel class")
	}
}
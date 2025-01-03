package io.rapidz.assignment1.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.rapidz.assignment1.data.Answer
import io.rapidz.assignment1.data.Question
import io.rapidz.assignment1.data.QuestionType
import io.rapidz.assignment1.repository.CandidateRepository
import io.rapidz.assignment1.repository.TestRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TestViewModel @Inject constructor (private val repository: TestRepository
) : ViewModel() {

	private val _answers = MutableStateFlow<List<Answer>>(emptyList())
	val answers : StateFlow<List<Answer>> = _answers

	val questions: List<Question> = listOf(
		Question(
			id = 1,
			questionText = "What is your favorite color?",
			options = listOf("Red", "Blue", "Green", "Yellow"),
			questionType = QuestionType.SINGLE_CHOICE
		),
		Question(
			id = 2,
			questionText = "Which programming languages do you know?",
			options = listOf("Kotlin", "Java", "Swift", "Python"),
			questionType = QuestionType.MULTIPLE_CHOICE
		),
		Question(
			id = 3,
			questionText = "Why do you want to learn programming?",
			questionType = QuestionType.FREE_TEXT
		)
	)

	fun saveAnswer(questionId: Int, answer: String, candidateId : Long) {
		viewModelScope.launch {
			val existingAnswer = _answers.value.find { it.questionId == questionId && it.candidateId == candidateId }
			if (existingAnswer != null) {
				repository.updateAnswer(existingAnswer.copy(answerText = answer))
			} else {
				repository.saveAnswer(Answer(questionId = questionId, answerText = answer, candidateId = candidateId))
			}
		}
	}

	fun getAnswersByCandidate(candidateId: Long): StateFlow<List<Answer>> {
		val answersFlow = MutableStateFlow<List<Answer>>(emptyList())
		viewModelScope.launch {
			repository.getAnswersByCandidate(candidateId).collect {
				answersFlow.value = it
			}
		}
		return answersFlow
	}

	init {
	    viewModelScope.launch {
			repository.getAnswers().collect{
				_answers.value = it
			}
		}
	}
}

class TestViewModelFactory(private val testRepository: TestRepository) : ViewModelProvider.Factory {
	override fun <T : ViewModel> create(modelClass: Class<T>): T {
		if (modelClass.isAssignableFrom(TestViewModel::class.java)) {
			@Suppress("UNCHECKED_CAST")
			return TestViewModel(testRepository) as T
		}
		throw IllegalArgumentException("Unknown ViewModel class")
	}
}
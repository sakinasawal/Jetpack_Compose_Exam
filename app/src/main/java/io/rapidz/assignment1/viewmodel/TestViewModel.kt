package io.rapidz.assignment1.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.rapidz.assignment1.data.Answer
import io.rapidz.assignment1.data.Question
import io.rapidz.assignment1.data.QuestionType
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

	private val _candidateTimers = MutableStateFlow<Map<Long, Int>>(emptyMap())
	val candidateTimers: StateFlow<Map<Long, Int>> = _candidateTimers

	init {
		viewModelScope.launch {
			repository.getAnswers().collect{
				_answers.value = it
			}
		}
	}

	val questions: List<Question> = listOf(
		Question(
			id = 1,
			questionText = "What is your favorite color?",
			options = listOf("Red", "Blue", "Green", "Yellow"),
			questionType = QuestionType.SINGLE_CHOICE,
			defaultAnswer = "Blue"
		),
		Question(
			id = 2,
			questionText = "Which programming languages do you know?",
			options = listOf("Kotlin", "Java", "Swift", "Python"),
			questionType = QuestionType.MULTIPLE_CHOICE,
			defaultAnswer = "Kotlin, Java"
		),
		Question(
			id = 3,
			questionText = "Why do you want to learn programming?",
			questionType = QuestionType.FREE_TEXT
		)
	)

	fun saveAnswer(questionId: Int, answer: String, candidateId : Long, remainingTime: Int, questionType: QuestionType, defaultAnswer: String, adminScore: Int? = null) {
		viewModelScope.launch {
			val score = when {
				questionType == QuestionType.FREE_TEXT && adminScore != null -> adminScore.toString()
				questionType != QuestionType.FREE_TEXT -> {
					if (answer == defaultAnswer) 10.toString() else 0.toString()
				}
				else -> "?"
			}
			val existingAnswer = _answers.value.find { it.questionId == questionId && it.candidateId == candidateId }
			if (existingAnswer != null) {
				repository.updateAnswer(existingAnswer.copy(answerText = answer, score = score, remainingTime = remainingTime))
			} else {
				repository.saveAnswer(
					Answer(questionId = questionId, answerText = answer, candidateId = candidateId, defaultAnswer = defaultAnswer, score = score, remainingTime = remainingTime))
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

	fun getTimerForCandidate(candidateId: Long) {
		viewModelScope.launch {
			val timer = repository.getTimerForCandidate(candidateId) ?: 0
			// Update the timer for the specific candidate
			_candidateTimers.value = _candidateTimers.value.toMutableMap().apply {
				put(candidateId, timer)
			}
		}
	}

	fun getCandidateScore(candidateId: Long): String {
		val candidateAnswers = _answers.value.filter { it.candidateId == candidateId }
		return if (candidateAnswers.any { it.score == "?" }) {
			"?"
		} else {
			candidateAnswers.sumOf { it.score.toIntOrNull() ?: 0 }.toString()
		}
	}

	fun clearAnswersForCandidate(candidateId: Long) {
		viewModelScope.launch {
			repository.deleteAnswersForCandidate(candidateId)
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
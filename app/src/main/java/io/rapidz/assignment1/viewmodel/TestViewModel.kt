package io.rapidz.assignment1.viewmodel

import android.os.CountDownTimer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.rapidz.assignment1.data.Answer
import io.rapidz.assignment1.data.Question
import io.rapidz.assignment1.data.QuestionType
import io.rapidz.assignment1.repository.TestRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TestViewModel @Inject constructor (private val repository: TestRepository
) : ViewModel() {

	private val answers = MutableStateFlow<List<Answer>>(emptyList())
	private val totalTimeTaken = MutableStateFlow<Map<Long, Int>>(emptyMap())

	private val _remainingTime = MutableStateFlow(0)
	val remainingTime: StateFlow<Int> = _remainingTime

	init {
		viewModelScope.launch {
			repository.getAnswers().collect{
				answers.value = it
				var test = 0
				it.forEach { question -> test += (question.totalTime - question.remainingTime) }
				_remainingTime.value = test
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

	fun saveAnswer(questionId: Int, answer: String, candidateId : Long, remainingTime: Int, initialTime : Int, questionType: QuestionType, defaultAnswer: String, adminScore: Int? = null, isAdmin : Boolean = false) {
		viewModelScope.launch {
			val existingAnswer = answers.value.find { it.questionId == questionId && it.candidateId == candidateId }

			if (isAdmin) {
				if (existingAnswer != null){
					repository.updateAdminScore(questionId, candidateId, adminScore ?: 0)
				}
			} else {
				val score = when {
					questionType == QuestionType.FREE_TEXT && adminScore != null -> adminScore.toString()
					questionType != QuestionType.FREE_TEXT -> {
						if (answer == defaultAnswer) 10.toString() else 0.toString()
					}
					else -> "?"
				}

				val timeSpent = initialTime - remainingTime
				val totalTimeForCandidate = totalTimeTaken.value[candidateId] ?: 0
				val totalTime = totalTimeForCandidate + timeSpent

				if (existingAnswer != null) {
					repository.updateAnswer(existingAnswer.copy(answerText = answer, score = score, remainingTime = remainingTime, totalTime = totalTime))
				} else {
					repository.saveAnswer(
						Answer(questionId = questionId, answerText = answer, candidateId = candidateId, defaultAnswer = defaultAnswer, score = score, remainingTime = remainingTime, totalTime = totalTime))
				}

				val newTotalTime = totalTimeForCandidate + timeSpent
				totalTimeTaken.value = totalTimeTaken.value.toMutableMap().apply {
					put(candidateId, newTotalTime)
				}
			}
		}
	}

	fun getRemainingTimeForCandidate(candidateId: Long): Int {
		return answers.value.find { it.candidateId == candidateId }?.remainingTime ?: 0
	}

	fun getTotalTimeTaken(candidateId: Long): StateFlow<Int> {
		viewModelScope.launch {
			val totalTime = repository.getTotalTimeTaken(candidateId)
			totalTimeTaken.value = totalTimeTaken.value.toMutableMap().apply {
				put(candidateId, totalTime)
			}
		}
		return totalTimeTaken.map { it[candidateId] ?: 0 }.stateIn(viewModelScope, SharingStarted.Lazily, 0)
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

	fun getCandidateScore(candidateId: Long): String {
		val candidateAnswers = answers.value.filter { it.candidateId == candidateId }
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
package io.rapidz.assignment1.viewmodel

import android.os.CountDownTimer
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.rapidz.assignment1.Key
import io.rapidz.assignment1.data.Answer
import io.rapidz.assignment1.data.Question
import io.rapidz.assignment1.data.QuestionData
import io.rapidz.assignment1.data.QuestionType
import io.rapidz.assignment1.data.TestUiState
import io.rapidz.assignment1.repository.Repository
import io.rapidz.assignment1.ui.test.DialogType
import io.rapidz.assignment1.ui.test.isQuestionComplete
import io.rapidz.assignment1.utils.Constants
import io.rapidz.assignment1.utils.TimeUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TestViewModel @Inject constructor (
	private val repository: Repository,
	savedStateHandle: SavedStateHandle
) : ViewModel() {

	private val testUiState = MutableStateFlow(TestUiState())
	val uiState: StateFlow<TestUiState> = testUiState

	private val dialogUiState = MutableStateFlow<DialogType?>(null)
	val dialogState: StateFlow<DialogType?> = dialogUiState

	private val candidateId: Long = savedStateHandle[Key.CANDIDATE_ID] ?: 0L
	private val usePreviousData: Boolean = savedStateHandle[Key.USE_PREVIOUS_DATA] ?: false

	var lastNavigation: NavigationDirection? = null

	init {
		loadQuestions()
		if (usePreviousData) {
			loadPreviousAnswers()
		}
	}

	private fun loadQuestions() {
		testUiState.update { currentState ->
			currentState.copy(questions = QuestionData.question)
		}
	}

	private fun loadPreviousAnswers() {
		viewModelScope.launch {
			repository.getAnswersByCandidate(candidateId).collect { answers ->
				testUiState.update { currentState ->
					currentState.copy(answers = answers.associateBy { it.questionId })
				}
			}
		}
	}

	fun saveAnswer(questionId: Int, answerText: String) {
		viewModelScope.launch {
			val answer = repository.getAnswersByCandidate(candidateId)
			val existingAnswer = answer.first().find { it.questionId == questionId }
			val question = uiState.value.questions.find { it.id == questionId }

			if (question != null){
				val totalQuestion = uiState.value.questions.size.takeIf { it > 0 } ?: 1
				val scorePerQuestion = TimeUtils.MAX_SCORE / totalQuestion

				val score = when (question.questionType){
					QuestionType.SINGLE_CHOICE, QuestionType.MULTIPLE_CHOICE -> {
						if(answerText == question.defaultAnswer) scorePerQuestion else 0
					}

					QuestionType.FREE_TEXT -> null
				}

				val answerScore = Answer(questionId = questionId, candidateId = candidateId, answerText = answerText, score = score)

				if (existingAnswer != null){
					repository.updateAnswer(existingAnswer.copy(answerText = answerText, score = score))
				} else {
					repository.saveAnswer(answerScore)
				}

				// Update the UI state
				testUiState.update { currentState ->
					val updatedAnswers = currentState.answers.toMutableMap().apply {
						put(questionId, answerScore)
					}
					currentState.copy(answers = updatedAnswers)
				}
			}

		}
	}

	// region Bottom Nav ========================================================================================================

	fun goToNextQuestion(onProceed : Boolean = false) {
		if (onProceed || checkCurrentQuestionCompletion()){
			val currentIndex = testUiState.value.currentQuestionIndex
			if (currentIndex < testUiState.value.questions.size - 1){
				testUiState.value = testUiState.value.copy(currentQuestionIndex = currentIndex + 1)
			}
		} else {
			lastNavigation = NavigationDirection.NEXT
			dialogUiState.value = DialogType.QUESTION_NOT_COMPLETE
		}
	}

	fun goToPreviousQuestion(onProceed : Boolean = false) {
		if (onProceed || checkCurrentQuestionCompletion()){
			val currentIndex = testUiState.value.currentQuestionIndex
			if (currentIndex > 0){
				testUiState.value = testUiState.value.copy(currentQuestionIndex = currentIndex - 1)
			}
		} else {
			lastNavigation = NavigationDirection.PREVIOUS
			dialogUiState.value = DialogType.QUESTION_NOT_COMPLETE
		}
	}

	// end region

	// region dialog ========================================================================================================

	private fun checkCurrentQuestionCompletion() : Boolean {
		val currentIndex = uiState.value.currentQuestionIndex
		val currentQuestion = uiState.value.questions.getOrNull(currentIndex)
		val currentAnswer = currentQuestion?.let { uiState.value.answers[it.id]?.answerText.orEmpty() }

		return if (currentQuestion != null) {
			when (currentQuestion.questionType){
				QuestionType.SINGLE_CHOICE, QuestionType.MULTIPLE_CHOICE -> !currentAnswer.isNullOrEmpty()
				QuestionType.FREE_TEXT -> !currentAnswer.isNullOrBlank()
			}
		} else {
			true
		}
	}

	fun checkAllQuestions(){
		val allAnswered = uiState.value.questions.all { question ->
			val answer = uiState.value.answers[question.id]?.answerText.orEmpty()
			isQuestionComplete(question, answer)
		}

		dialogUiState.value = if (allAnswered) {
			DialogType.ALL_QUESTIONS_COMPLETE
		} else {
			DialogType.ALL_QUESTIONS_NOT_COMPLETE
		}
	}

	fun dismissDialog(){
		dialogUiState.value = null
	}

	// end region
}

enum class NavigationDirection {
	NEXT, PREVIOUS
}
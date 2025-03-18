package io.rapidz.assignment1.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.rapidz.assignment1.Key
import io.rapidz.assignment1.data.CandidateWithScore
import io.rapidz.assignment1.data.Question
import io.rapidz.assignment1.data.QuestionData
import io.rapidz.assignment1.data.QuestionType
import io.rapidz.assignment1.data.UiState
import io.rapidz.assignment1.repository.Repository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminViewModel @Inject constructor (
	private val repository: Repository,
	savedStateHandle: SavedStateHandle,
) : ViewModel(){

	private val listCandidatesWithScores = MutableStateFlow<List<CandidateWithScore>>(emptyList())
	val candidatesWithScores: StateFlow<List<CandidateWithScore>> = listCandidatesWithScores

	private val isGifShows = MutableStateFlow(true)
	val isGifVisible: StateFlow<Boolean> = isGifShows

	private val searchQueryName = MutableStateFlow("")
	val searchQuery: StateFlow<String> = searchQueryName

	private val candidateId: Long = savedStateHandle[Key.CANDIDATE_ID] ?: 0L

	private val adminUiState = MutableStateFlow(UiState())
	val uiState: StateFlow<UiState> = adminUiState

	init {
		loadCandidate()
		loadQuestionsAndAnswers()
	}

	// region Admin Dashboard ========================================================================

	private fun loadCandidate(){
		viewModelScope.launch {
			val candidates = repository.getAllCandidates()
			val candidatesWithScores = candidates.map { candidate ->
				val answers = repository.getAnswersByCandidate(candidate.id).first()
				val totalScore = answers.sumOf { it.score ?: 0 }
				CandidateWithScore(candidate, totalScore)
			}

			delay(2000)
			listCandidatesWithScores.value = candidatesWithScores
			isGifShows.value = false
		}
	}

	fun searchQueryChanged(query : String){
		searchQueryName.value = query
	}

	fun searchCandidates(){
		viewModelScope.launch {
			val query = searchQueryName.value.lowercase().trim()
			val allCandidates = repository.getAllCandidates()
			val filteredCandidates = allCandidates.filter { candidate ->
				candidate.name.lowercase().contains(query)
			}.map { candidate ->
				val answers = repository.getAnswersByCandidate(candidate.id).first()
				val totalScore = answers.sumOf { it.score ?: 0 }
				CandidateWithScore(candidate, totalScore)
			}
			listCandidatesWithScores.value = filteredCandidates
		}
	}

	// end region

	// region Admin Test =============================================================================

	private fun loadQuestionsAndAnswers() {
		viewModelScope.launch {
			val questions = QuestionData.question
			val answers = repository.getAnswersByCandidate(candidateId).first()

			val answerMap = answers.associateBy { it.questionId }

			adminUiState.update { currentState ->
				currentState.copy(
					questions = questions,
					answers = answerMap
				)
			}
		}
	}

	fun isAnswerCorrect(question: Question, answerText: String?): Boolean {
		return when (question.questionType) {
			QuestionType.SINGLE_CHOICE, QuestionType.MULTIPLE_CHOICE ->
				answerText == question.defaultAnswer
			QuestionType.FREE_TEXT -> false
		}
	}

	fun goToNextQuestion() {
		val currentIndex = adminUiState.value.currentQuestionIndex
		if (currentIndex < adminUiState.value.questions.size - 1) {
			adminUiState.update { it.copy(currentQuestionIndex = currentIndex + 1) }
		}
	}

	fun goToPreviousQuestion() {
		val currentIndex = adminUiState.value.currentQuestionIndex
		if (currentIndex > 0) {
			adminUiState.update { it.copy(currentQuestionIndex = currentIndex - 1) }
		}
	}

	fun goToFirstQuestion() {
		adminUiState.update { it.copy(currentQuestionIndex = 0) }
	}

	fun goToLastQuestion() {
		adminUiState.update { it.copy(currentQuestionIndex = it.questions.lastIndex) }
	}

	// end region
}
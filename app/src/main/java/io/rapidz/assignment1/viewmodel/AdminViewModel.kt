package io.rapidz.assignment1.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.rapidz.assignment1.Key
import io.rapidz.assignment1.data.Answer
import io.rapidz.assignment1.data.CandidateWithScore
import io.rapidz.assignment1.data.Question
import io.rapidz.assignment1.data.QuestionData
import io.rapidz.assignment1.data.QuestionType
import io.rapidz.assignment1.data.UiState
import io.rapidz.assignment1.repository.Repository
import io.rapidz.assignment1.storage.DataStoreInterface
import io.rapidz.assignment1.storage.DataStoreManager
import io.rapidz.assignment1.utils.TimeUtils
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminViewModel @Inject constructor (
	private val repository: Repository,
	private val dataStore: DataStoreInterface,
	savedStateHandle: SavedStateHandle
) : ViewModel(){

	private val setTimeLimit = MutableStateFlow(0)
	val timeLimit: StateFlow<Int> = setTimeLimit

	private val listCandidatesWithScores = MutableStateFlow<List<CandidateWithScore>>(emptyList())
	val candidatesWithScores: StateFlow<List<CandidateWithScore>> = listCandidatesWithScores

	private val isGifShows = MutableStateFlow(true)
	val isGifVisible: StateFlow<Boolean> = isGifShows

	private val candidateId: Long = savedStateHandle[Key.CANDIDATE_ID] ?: 0L

	private val adminUiState = MutableStateFlow(UiState())
	val uiState: StateFlow<UiState> = adminUiState

	private val searchQuery = MutableStateFlow("")

	private val uiFreeTextScores = MutableStateFlow<Map<Int, Boolean?>>(emptyMap())
	val freeTextScores: StateFlow<Map<Int, Boolean?>> = uiFreeTextScores

	private val totalQuestions: Int = QuestionData.question.size

	init {
		timerLimit()
		loadCandidate()
		observeSearchQuery()
		loadQuestionsAndAnswers()
	}

	// region Admin Dashboard ========================================================================

	private fun timerLimit(){
		viewModelScope.launch {
			setTimeLimit.value = dataStore.readFromDataStore(DataStoreManager.TIME_LIMIT) ?: 0
		}
	}

	fun setTimeLimit(newTimeLimit : Int){
		setTimeLimit.value = newTimeLimit
		viewModelScope.launch {
			dataStore.writeToDataStore(DataStoreManager.TIME_LIMIT, newTimeLimit)
		}
	}

	fun loadCandidate(){
		viewModelScope.launch {
			val candidates = repository.getAllCandidates()
			val candidatesWithScores = candidates.mapNotNull { candidate ->
				val answers = repository.getAnswersByCandidate(candidate.id).first()
				if (answers.size != totalQuestions) return@mapNotNull null
				val hasNullScore = answers.any { it.score == null }
				val totalScore = if(hasNullScore) null else answers.sumOf { it.score ?: 0 }
				totalScore?.let { CandidateWithScore(candidate, it) }
			}
			listCandidatesWithScores.value = candidatesWithScores
			delay(2000)
			isGifShows.value = false
		}
	}

	fun updateSearchQuery(query: String) {
		searchQuery.value = query
	}

	@OptIn(FlowPreview::class)
	private fun observeSearchQuery() {
		viewModelScope.launch {
			searchQuery
				.debounce(2000) // Wait 2 seconds after last input
				.distinctUntilChanged() // Ignore duplicate queries
				.collectLatest { query ->
					searchCandidates(query)
				}
		}
	}

	private fun searchCandidates(query: String) {
		viewModelScope.launch {
			val allCandidates = repository.getAllCandidates()

			val filteredCandidates = allCandidates.mapNotNull { candidate ->
				val answers = repository.getAnswersByCandidate(candidate.id).first()
				if (answers.size != totalQuestions) return@mapNotNull null
				val hasNullScore = answers.any { it.score == null }
				val totalScore = if (hasNullScore) null else answers.sumOf { it.score ?: 0 }

				CandidateWithScore(candidate, totalScore)
			}.filter { it.candidate.name.contains(query, ignoreCase = true) }

			listCandidatesWithScores.value = filteredCandidates
		}
	}

	// end region

	// region Admin Test =============================================================================

	private fun loadQuestionsAndAnswers() {
		viewModelScope.launch {
			val questions = QuestionData.question
			val answers = repository.getAnswersByCandidate(candidateId).first()

			val answerMap = answers.map { it.copy() }.associateBy { it.questionId }

			val freeTextScoreMap = answers
				.filter { it.questionId in questions.filter { q -> q.questionType == QuestionType.FREE_TEXT }.map { q->q.id } }
				.associate { it.questionId to (it.score?.let { s -> s > 0 }) }

			adminUiState.update { currentState ->
				currentState.copy(
					questions = questions,
					answers = answerMap
				)
			}

			uiFreeTextScores.value = freeTextScoreMap
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

	fun scoreFreeText(questionId : Int, isCorrect : Boolean){
		viewModelScope.launch {
			val answer = repository.getAnswersByCandidate(candidateId)
			val existingAnswer = answer.first().find { it.questionId == questionId }
			val question = uiState.value.questions.find { it.id == questionId }

			question?.let {
				val totalQuestions = uiState.value.questions.size.takeIf { it > 0 } ?: 1
				val scorePerQuestion = TimeUtils.MAX_SCORE / totalQuestions

				val score = if (isCorrect) scorePerQuestion else 0

				val updatedAnswer = Answer(questionId = questionId, candidateId = candidateId, answerText = existingAnswer?.answerText?:"", score = score)

				existingAnswer?.let {
					repository.updateAnswer(it.copy(score = score))
				} ?: repository.saveAnswer(updatedAnswer)

				loadCandidate()

				adminUiState.update { currentState ->
					val updatedAnswers = currentState.answers.toMutableMap().apply {
						put(questionId, updatedAnswer)
					}
					currentState.copy(answers = updatedAnswers)
				}

				uiFreeTextScores.update { it + (questionId to isCorrect) }
			}
		}
	}

	// end region
}
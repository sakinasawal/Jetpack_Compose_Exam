package io.rapidz.assignment1.data

data class CandidateUiState(
	val name: String = "",
	val email: String = "",
	val showDialog: Boolean = false,
	val candidateId: Long? = null
)

data class TestUiState(
	val questions: List<Question> = emptyList(),
	val answers: Map<Int, Answer> = emptyMap(),
	val currentQuestionIndex: Int = 0
)
package io.rapidz.assignment1.data

data class CandidateUiState(
	val name: String = "",
	val email: String = "",
	val showDialog: Boolean = false,
	val candidateId: Long? = null
)

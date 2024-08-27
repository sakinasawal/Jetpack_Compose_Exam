package io.rapidz.assignment1.data

data class Question(
	val id: Int,
	val questionText: String,
	val options: List<String> = emptyList(), // For MCQ
	val questionType: QuestionType
)

enum class QuestionType {
	SINGLE_CHOICE,
	MULTIPLE_CHOICE,
	FREE_TEXT
}

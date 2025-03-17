package io.rapidz.assignment1.data

data class Question(
	val id: Int,
	val questionText: String = "",
	val options: List<String> = emptyList(),
	val questionType: QuestionType = QuestionType.SINGLE_CHOICE,
	val defaultAnswer : String = ""
)

enum class QuestionType {
	SINGLE_CHOICE,
	MULTIPLE_CHOICE,
	FREE_TEXT
}

object QuestionData {
	val question: List<Question> = listOf(
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
			questionText = "What is your preferred development platform?",
			options = listOf("Android", "iOS", "Web", "Desktop"),
			questionType = QuestionType.SINGLE_CHOICE,
			defaultAnswer = "Android"
		),
		Question(
			id = 4,
			questionText = "Which frameworks have you worked with?",
			options = listOf("React", "Flutter", "Angular", "Vue"),
			questionType = QuestionType.MULTIPLE_CHOICE,
			defaultAnswer = "React, Flutter"
		),
		Question(
			id = 5,
			questionText = "What is your experience with databases?",
			options = listOf("SQL", "NoSQL", "Both", "None"),
			questionType = QuestionType.SINGLE_CHOICE,
			defaultAnswer = "SQL"
		),
		Question(
			id = 6,
			questionText = "What is your favorite IDE?",
			options = listOf("Android Studio", "VS Code", "Xcode", "IntelliJ"),
			questionType = QuestionType.SINGLE_CHOICE,
			defaultAnswer = "Android Studio"
		),
		Question(
			id = 7,
			questionText = "Why do you want to learn programming?",
			questionType = QuestionType.FREE_TEXT
		)
	)
}
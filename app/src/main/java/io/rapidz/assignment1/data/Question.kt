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
			questionText = "What is your favorite programming language?",
			options = listOf("Kotlin", "Java", "Swift", "Python"),
			questionType = QuestionType.SINGLE_CHOICE,
			defaultAnswer = "Kotlin"
		),
		Question(
			id = 2,
			questionText = "Which development platforms have you worked on?",
			options = listOf("Android", "iOS", "Web", "Desktop"),
			questionType = QuestionType.MULTIPLE_CHOICE,
			defaultAnswer = "Android, Web"
		),
		Question(
			id = 3,
			questionText = "What is your preferred version control system?",
			options = listOf("Git", "SVN", "Mercurial", "None"),
			questionType = QuestionType.SINGLE_CHOICE,
			defaultAnswer = "Git"
		),
		Question(
			id = 4,
			questionText = "Which frontend frameworks have you used?",
			options = listOf("React", "Vue", "Angular", "Svelte"),
			questionType = QuestionType.MULTIPLE_CHOICE,
			defaultAnswer = "React, Vue"
		),
		Question(
			id = 5,
			questionText = "Which backend technologies have you worked with?",
			options = listOf("Node.js", "Django", "Spring Boot", "Laravel"),
			questionType = QuestionType.MULTIPLE_CHOICE,
			defaultAnswer = "Node.js, Spring Boot"
		),
		Question(
			id = 6,
			questionText = "What is your preferred cloud service provider?",
			options = listOf("AWS", "Google Cloud", "Azure", "None"),
			questionType = QuestionType.SINGLE_CHOICE,
			defaultAnswer = "AWS"
		),
		Question(
			id = 7,
			questionText = "What is your experience with databases?",
			options = listOf("SQL", "NoSQL", "Both", "None"),
			questionType = QuestionType.SINGLE_CHOICE,
			defaultAnswer = "SQL"
		),
		Question(
			id = 8,
			questionText = "What is your favorite IDE?",
			options = listOf("Android Studio", "VS Code", "Xcode", "IntelliJ"),
			questionType = QuestionType.SINGLE_CHOICE,
			defaultAnswer = "Android Studio"
		),
		Question(
			id = 9,
			questionText = "Why do you want to become a developer?",
			questionType = QuestionType.FREE_TEXT
		),
		Question(
			id = 10,
			questionText = "Describe a challenging bug you have fixed and how you solved it.",
			questionType = QuestionType.FREE_TEXT
		)
	)
}
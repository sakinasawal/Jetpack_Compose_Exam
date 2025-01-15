package io.rapidz.assignment1.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Dangerous
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.rapidz.assignment1.spacing_20
import io.rapidz.assignment1.ui.AppTypography
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import io.rapidz.assignment1.TextLabelTitle
import io.rapidz.assignment1.data.QuestionType
import io.rapidz.assignment1.repository.TestRepository
import io.rapidz.assignment1.spacing_4
import io.rapidz.assignment1.storage.AppDatabase
import io.rapidz.assignment1.test.BottomAppBarAdmin
import io.rapidz.assignment1.test.CheckBoxAnswer
import io.rapidz.assignment1.test.RadioButtonAnswer
import io.rapidz.assignment1.test.Textarea
import io.rapidz.assignment1.ui.AdminTheme
import io.rapidz.assignment1.ui.md_theme_admin_error
import io.rapidz.assignment1.ui.md_theme_default_primaryContainer
import io.rapidz.assignment1.viewmodel.TestViewModel
import io.rapidz.assignment1.viewmodel.TestViewModelFactory

@Composable
fun AdminTestScreen(
	candidateId : Long,
) {
	val context = LocalContext.current
	val database = remember { AppDatabase.getDatabase(context) }
	val repository = remember { TestRepository(database.answerDao()) }
	val viewModel: TestViewModel = viewModel(factory = TestViewModelFactory(repository))
	val candidateAnswers by viewModel.getAnswersByCandidate(candidateId).collectAsState(initial = emptyList())
	val questions = viewModel.questions
	var currentIndex by remember { mutableIntStateOf(0) }

	AdminTheme {

		val question = questions[currentIndex]
		val answer = candidateAnswers.find { it.questionId == question.id }

		val isAnswerCorrect = when (question.questionType) {
			QuestionType.SINGLE_CHOICE, QuestionType.MULTIPLE_CHOICE -> {
				answer?.answerText == question.defaultAnswer
			}
			else -> false
		}

		val showDoneIcon = when (question.questionType) {
			QuestionType.SINGLE_CHOICE, QuestionType.MULTIPLE_CHOICE -> isAnswerCorrect
			QuestionType.FREE_TEXT -> true
		}

		val showCloseIcon = when (question.questionType) {
			QuestionType.SINGLE_CHOICE, QuestionType.MULTIPLE_CHOICE -> !isAnswerCorrect
			QuestionType.FREE_TEXT -> true
		}

		val closeIcon = when (question.questionType) {
			QuestionType.FREE_TEXT -> Icons.Default.Close
			else -> if (isAnswerCorrect) Icons.Default.Close else Icons.Default.Dangerous
		}

		val closeIconColor = when (question.questionType) {
			QuestionType.FREE_TEXT -> Color.Black
			else -> if (isAnswerCorrect) Color.Black else md_theme_admin_error
		}

		val doneIconColor = if (isAnswerCorrect) Color(0xFF018786) else Color.Black

		BottomAppBarAdmin(
			showDoneIcon = showDoneIcon,
			showCloseIcon = showCloseIcon,
			closeIcon = closeIcon,
			closeIconColor = closeIconColor,
			doneIconColor = doneIconColor,
			onDoneClick = {},
			onLeftArrowClick = {
				if (currentIndex > 0) {
					currentIndex--
				}
			},
			onRightArrowClick = {
				if (currentIndex < questions.size - 1) {
					currentIndex++
				}
			}
		){
			Column(
				modifier = Modifier
					.fillMaxSize()
					.background(color = md_theme_default_primaryContainer)
					.padding(spacing_20)
			){

				TextLabelTitle(
					text = "Question " + question.id,
					typographyStyle = AppTypography.titleLarge
				)

				Spacer(modifier = Modifier.height(spacing_20))

				TextLabelTitle(
					text = question.questionText
				)

				Spacer(modifier = Modifier.height(spacing_4))

				when (question.questionType){
					QuestionType.SINGLE_CHOICE -> answer?.let { RadioButtonAnswer(
						options = question.options,
						currentAnswer = it.answerText,
						onAnswerChange = { })
					}
					QuestionType.MULTIPLE_CHOICE -> answer?.let {
						CheckBoxAnswer(
							options = question.options,
							currentAnswer = it.answerText,
							onAnswerChange = {}
						)
					}
					QuestionType.FREE_TEXT -> {
						answer?.let {
							Textarea(
								initialText = it.answerText,
								readOnly = true,
								onAnswerChange = {}
							)
						}
					}
				}
			}
		}
	}
}
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
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import io.rapidz.assignment1.TextLabelTitle
import io.rapidz.assignment1.data.QuestionType
import io.rapidz.assignment1.formatSecondsToTime
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

	val question = questions[currentIndex]
	val answer = candidateAnswers.find { it.questionId == question.id }

	val remainingTime = answer?.remainingTime ?: 0

	AdminTheme {

		val isAnswerCorrect = when (question.questionType) {
			QuestionType.SINGLE_CHOICE, QuestionType.MULTIPLE_CHOICE -> {
				answer?.answerText == question.defaultAnswer
			}
			else -> false
		}

		var isDoneClicked by remember { mutableStateOf(false) }
		var isCloseClicked by remember { mutableStateOf(false) }

		val (doneIconVisible, doneIconColor) = when (question.questionType) {
			QuestionType.FREE_TEXT -> {
				when {
					isDoneClicked -> Pair(true, Color(0xFF018786))
					isCloseClicked -> Pair(false, Color.Black)
					else -> Pair(true, Color.Black)
				}
			}
			else -> Pair(isAnswerCorrect, Color(0xFF018786))
		}

		val (closeIconVisible, closeIcon, closeIconColor) = when (question.questionType) {
			QuestionType.FREE_TEXT -> {
				when {
					isCloseClicked -> Triple(true, Icons.Default.Dangerous, md_theme_admin_error)
					isDoneClicked -> Triple(false, Icons.Default.Close, Color.Black)
					else -> Triple(true, Icons.Default.Close, Color.Black)
				}
			}
			else -> Triple(!isAnswerCorrect, Icons.Default.Dangerous, md_theme_admin_error)
		}

		BottomAppBarAdmin(
			showDoneIcon = doneIconVisible,
			showCloseIcon = closeIconVisible,
			closeIcon = closeIcon,
			closeIconColor = closeIconColor,
			doneIconColor = doneIconColor,
			onDoneClick = {
				if (question.questionType == QuestionType.FREE_TEXT) {
					viewModel.saveAnswer(
						questionId = question.id,
						answer = answer?.answerText.orEmpty(),
						candidateId = candidateId,
						remainingTime = remainingTime,
						questionType = question.questionType,
						defaultAnswer = question.defaultAnswer,
						adminScore = 10
					)
					isDoneClicked = true
					isCloseClicked = false
				}
			},
			onCloseClick = {
				if (question.questionType == QuestionType.FREE_TEXT) {
					viewModel.saveAnswer(
						questionId = question.id,
						answer = answer?.answerText.orEmpty(),
						candidateId = candidateId,
						remainingTime = remainingTime,
						questionType = question.questionType,
						defaultAnswer = question.defaultAnswer,
						adminScore = 0
					)
					isCloseClicked = true
					isDoneClicked = false
				}
			},
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
								readOnly = true
							)
						}
					}
				}
			}
		}
	}
}
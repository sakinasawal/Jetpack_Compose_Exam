package io.rapidz.assignment1.test

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import io.rapidz.assignment1.EndTestAlertDialog
import io.rapidz.assignment1.GeneralAlertDialog
import io.rapidz.assignment1.R
import io.rapidz.assignment1.TextLabel
import io.rapidz.assignment1.TextLabel1
import io.rapidz.assignment1.data.Question
import io.rapidz.assignment1.data.QuestionType
import io.rapidz.assignment1.repository.CandidateRepository
import io.rapidz.assignment1.repository.TestRepository
import io.rapidz.assignment1.spacing_10
import io.rapidz.assignment1.spacing_20
import io.rapidz.assignment1.spacing_4
import io.rapidz.assignment1.storage.AppDatabase
import io.rapidz.assignment1.ui.DefaultTheme
import io.rapidz.assignment1.ui.*
import io.rapidz.assignment1.viewmodel.CandidateViewModel
import io.rapidz.assignment1.viewmodel.CandidateViewModelFactory
import io.rapidz.assignment1.viewmodel.TestViewModel
import io.rapidz.assignment1.viewmodel.TestViewModelFactory
import kotlinx.coroutines.delay

@Preview
@Composable
fun TestScreenBottomNav(
	navController: NavController? = null
){
	val context = LocalContext.current
	val database = remember { AppDatabase.getDatabase(context) }
	val repository = remember { TestRepository(database.answerDao()) }
	val viewModel: TestViewModel = viewModel(factory = TestViewModelFactory(repository))

	val questions = viewModel.questions
	val currentIndex = remember { mutableIntStateOf(0) }
	var currentAnswer by remember { mutableStateOf("") }

	DefaultTheme {
		BottomAppBar(
			onLeftArrowClick = {
				saveAnswerForCurrentQuestion(
					question = questions[currentIndex.intValue],
					currentAnswer = currentAnswer,
					viewModel = viewModel
				)

				if (currentIndex.intValue > 0) {
					currentIndex.intValue--
					currentAnswer = getSavedAnswer(questions[currentIndex.intValue], viewModel)
				}
			},
			onRightArrowClick = {
				saveAnswerForCurrentQuestion(
					question = questions[currentIndex.intValue],
					currentAnswer = currentAnswer,
					viewModel = viewModel
				)

				if (currentIndex.intValue < questions.size - 1) {
					currentIndex.intValue++
					currentAnswer = getSavedAnswer(questions[currentIndex.intValue], viewModel)
				}
			},
			onLeftDoubleArrowClick = {
				saveAnswerForCurrentQuestion(
					question = questions[currentIndex.intValue],
					currentAnswer = currentAnswer,
					viewModel = viewModel
				)
				currentIndex.intValue = 0
				currentAnswer = getSavedAnswer(questions[0], viewModel)
				Unit
			},
			onRightDoubleArrowClick = {
				saveAnswerForCurrentQuestion(
					question = questions[currentIndex.intValue],
					currentAnswer = currentAnswer,
					viewModel = viewModel
				)
				currentIndex.intValue = questions.size - 1
				currentAnswer = getSavedAnswer(questions.last(), viewModel)
				Unit
			}
		){
			Column(
				modifier = Modifier
					.fillMaxSize()
					.padding(spacing_10)
			){
				val question = questions[currentIndex.intValue]

				TextLabel1(
					text = "Question " + question.id,
					typographyStyle = AppTypography.titleLarge
				)

				Spacer(modifier = Modifier.height(spacing_20))

				TextLabel1(
					text = question.questionText
				)

				Spacer(modifier = Modifier.height(spacing_4))

				when (question.questionType) {
					QuestionType.SINGLE_CHOICE -> RadioButtonAnswer(questionId = question.id, viewModel = viewModel, onAnswerChange = { currentAnswer = it })
					QuestionType.MULTIPLE_CHOICE -> CheckBoxAnswer(questionId = question.id, viewModel = viewModel, onAnswerChange = { currentAnswer = it })
					QuestionType.FREE_TEXT -> Textarea(questionId = question.id, viewModel = viewModel, onAnswerChange = { currentAnswer = it })
				}
			}
		}
	}
}

/**
 * handle data save and get from room db
 */

fun saveAnswerForCurrentQuestion(
	question: Question,
	currentAnswer: String,
	viewModel: TestViewModel
) {
	viewModel.saveAnswer(question.id, currentAnswer)
}

fun getSavedAnswer(question: Question, viewModel: TestViewModel): String {
	val answers = viewModel.answers.value
	return answers.find { it.questionId == question.id }?.answerText ?: ""
}


/**
 * Handle dialog if question is answered or not
 */
@Preview
@Composable
private fun QuestionNotCompleteDialog(navController: NavController? = null){
	UncompletedQuestionTheme {
		GeneralAlertDialog(
			titleResId = R.string.title_question_not_complete,
			messageResId = R.string.content_question_not_complete,
			onPositiveButtonClick = {},
			onNegativeButtonClick = {}
		)
	}
}

@Preview
@Composable
private fun QuestionNotCompleteYetDialog(navController: NavController? = null){
	CompletedQuestionTheme {
		GeneralAlertDialog(
			titleResId = R.string.title_end_test,
			messageResId = R.string.content_end_test,
			onPositiveButtonClick = {},
			onNegativeButtonClick = {}
		)
	}
}

@Preview
@Composable
private fun EndOfTestDialog(navController: NavController? = null){
	CompletedQuestionTheme {
		EndTestAlertDialog(
			titleResId = R.string.title_end_test,
			messageResId = R.string.content_end_test_final,
			onPositiveButtonClick = {}
		)
	}
}

// change bottom bar color
//val themeContent: @Composable (@Composable () -> Unit) -> Unit = if (countdownState.value < 60 * 1000) {
//		{ UncompletedQuestionTheme(it) }
//	} else {
//		{ DefaultTheme(it) }
//	}
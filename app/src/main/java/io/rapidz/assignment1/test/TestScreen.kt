package io.rapidz.assignment1.test

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Checkbox
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import io.rapidz.assignment1.Screen
import io.rapidz.assignment1.TextLabel1
import io.rapidz.assignment1.data.Question
import io.rapidz.assignment1.data.QuestionType
import io.rapidz.assignment1.navigate
import io.rapidz.assignment1.repository.TestRepository
import io.rapidz.assignment1.spacing_1
import io.rapidz.assignment1.spacing_10
import io.rapidz.assignment1.spacing_20
import io.rapidz.assignment1.spacing_24
import io.rapidz.assignment1.spacing_4
import io.rapidz.assignment1.spacing_8
import io.rapidz.assignment1.storage.AppDatabase
import io.rapidz.assignment1.ui.DefaultTheme
import io.rapidz.assignment1.ui.*
import io.rapidz.assignment1.viewmodel.TestViewModel
import io.rapidz.assignment1.viewmodel.TestViewModelFactory

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
	var showDialog by remember { mutableStateOf(false) }

	DefaultTheme {
		if (showDialog) {
			EndOfTestDialog(navController = navController)
		}

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

				if(currentIndex.intValue == questions.size - 1){
					showDialog = true
				} else if (currentIndex.intValue < questions.size - 1) {
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
				//testing
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
					.background(color = md_theme_default_primaryContainer)
					.padding(spacing_20)
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
					QuestionType.SINGLE_CHOICE -> RadioButtonAnswer(options = question.options, onAnswerChange = { currentAnswer = it })
					QuestionType.MULTIPLE_CHOICE -> CheckBoxAnswer(options = question.options, onAnswerChange = { currentAnswer = it })
					QuestionType.FREE_TEXT -> Textarea(onAnswerChange = { currentAnswer = it })
				}
			}
		}
	}
}

/**
 * List of answers (3 types)
 */
@Composable
fun RadioButtonAnswer(options: List<String>, onAnswerChange: (String) -> Unit){
	var selectedOption by remember { mutableStateOf(options[0]) }
	Column {
		options.forEach{ option ->
			Row(
				modifier = Modifier
					.padding(all = spacing_4)
					.height(spacing_24)
					.selectable(
						selected = selectedOption == option,
						onClick = {
							selectedOption = option
							onAnswerChange(option)
						}
					),
				verticalAlignment = Alignment.CenterVertically
			){
				RadioButton(
					selected = selectedOption == option,
					onClick = null
				)
				Text(
					text = option,
					modifier = Modifier.padding(start = spacing_8)
				)
			}
		}
	}
}

@Composable
fun CheckBoxAnswer(options: List<String>, onAnswerChange: (String) -> Unit){
	val checkedStates = remember { mutableStateListOf(false, false, false, false) }
	Column(
		modifier = Modifier
			.fillMaxSize()
	) {
		options.forEachIndexed{ index, option ->
			Row(verticalAlignment = Alignment.CenterVertically) {
				Checkbox(
					checked = checkedStates[index],
					onCheckedChange = { isChecked ->
						checkedStates[index] = isChecked
						val selectedOptions = options.filterIndexed { i, _ -> checkedStates[i] }
						onAnswerChange(selectedOptions.joinToString(", "))
					}
				)
				Text(text = option)
			}
		}
	}
}

@Composable
fun Textarea(onAnswerChange: (String) -> Unit) {
	var text by remember { mutableStateOf("") }

	TextField(
		value = text,
		onValueChange = {
			text = it
			onAnswerChange(it)
		},
		modifier = Modifier
			.fillMaxWidth()
			.fillMaxHeight(0.9f)
			.padding(spacing_4)
			.border(width = spacing_1, color = Color.Black)
	)
}

/**
 * Handle data save and get from room db
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
private fun QuestionCompleteDialog(navController: NavController? = null){
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
			onPositiveButtonClick = {
				navController?.navigate(Screen.Role)
			}
		)
	}
}

// change bottom bar color
//val themeContent: @Composable (@Composable () -> Unit) -> Unit = if (countdownState.value < 60 * 1000) {
//		{ UncompletedQuestionTheme(it) }
//	} else {
//		{ DefaultTheme(it) }
//	}
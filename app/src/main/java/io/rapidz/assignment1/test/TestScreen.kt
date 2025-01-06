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
import io.rapidz.assignment1.TextLabelTitle
import io.rapidz.assignment1.data.Answer
import io.rapidz.assignment1.data.Question
import io.rapidz.assignment1.data.QuestionType
import io.rapidz.assignment1.navigate
import io.rapidz.assignment1.repository.TestRepository
import io.rapidz.assignment1.spacing_1
import io.rapidz.assignment1.spacing_20
import io.rapidz.assignment1.spacing_24
import io.rapidz.assignment1.spacing_4
import io.rapidz.assignment1.spacing_8
import io.rapidz.assignment1.storage.AppDatabase
import io.rapidz.assignment1.ui.DefaultTheme
import io.rapidz.assignment1.ui.*
import io.rapidz.assignment1.viewmodel.TestViewModel
import io.rapidz.assignment1.viewmodel.TestViewModelFactory

@Composable
fun TestScreenBottomNav(
	navController: NavController? = null,
	candidateId: Long,
	usePreviousData: Boolean = false
){
	val context = LocalContext.current
	val database = remember { AppDatabase.getDatabase(context) }
	val repository = remember { TestRepository(database.answerDao()) }
	val viewModel: TestViewModel = viewModel(factory = TestViewModelFactory(repository))

	val questions = viewModel.questions
	val currentIndex = remember { mutableIntStateOf(0) }
	var currentAnswer by remember { mutableStateOf("") }
	var dialogType by remember { mutableStateOf<DialogType?>(null) }
	val candidateAnswers by viewModel.getAnswersByCandidate(candidateId).collectAsState(initial = emptyList())

	var showEndOfTestDialog by remember { mutableStateOf(false) }

	LaunchedEffect(usePreviousData) {
		if (usePreviousData) {
			currentAnswer = getSavedAnswer(questions[currentIndex.intValue], candidateAnswers)
		} else {
			viewModel.clearAnswersForCandidate(candidateId)
		}
	}

	DefaultTheme {
		BottomAppBar(
			onLeftArrowClick = {
				if (!isQuestionComplete(questions[currentIndex.intValue], currentAnswer)){
					dialogType = DialogType.QUESTION_NOT_COMPLETE
				} else if (currentIndex.intValue > 0) {
					saveAnswerForCurrentQuestion(questions[currentIndex.intValue], currentAnswer, candidateId, viewModel)
					currentIndex.intValue--
					currentAnswer = getSavedAnswer(questions[currentIndex.intValue], candidateAnswers)
				}
			},
			onRightArrowClick = {
				if (!isQuestionComplete(questions[currentIndex.intValue], currentAnswer)) {
					dialogType = DialogType.QUESTION_NOT_COMPLETE
				} else {
					saveAnswerForCurrentQuestion(questions[currentIndex.intValue], currentAnswer, candidateId, viewModel)
					if (currentIndex.intValue == questions.size - 1) {
						dialogType = DialogType.ALL_QUESTIONS_COMPLETE
					} else {
						currentIndex.intValue++
						currentAnswer = getSavedAnswer(questions[currentIndex.intValue], candidateAnswers)
					}
				}
			},
			onLeftDoubleArrowClick = {
				currentIndex.intValue = 0
				currentAnswer = getSavedAnswer(questions[0], candidateAnswers)
				Unit
			},
			onRightDoubleArrowClick = {
				saveAnswerForCurrentQuestion(
					question = questions[currentIndex.intValue],
					currentAnswer = currentAnswer,
					candidateId = candidateId,
					viewModel = viewModel
				)
				currentIndex.intValue = questions.size - 1
				currentAnswer = getSavedAnswer(questions.last(), candidateAnswers)
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

				TextLabelTitle(
					text = "Question " + question.id,
					typographyStyle = AppTypography.titleLarge
				)

				Spacer(modifier = Modifier.height(spacing_20))

				TextLabelTitle(
					text = question.questionText
				)

				Spacer(modifier = Modifier.height(spacing_4))

				when (question.questionType) {
					QuestionType.SINGLE_CHOICE -> RadioButtonAnswer(
						options = question.options,
						currentAnswer = currentAnswer,
						onAnswerChange = { currentAnswer = it })
					QuestionType.MULTIPLE_CHOICE -> CheckBoxAnswer(
						options = question.options,
						currentAnswer = currentAnswer,
						onAnswerChange = { currentAnswer = it })
					QuestionType.FREE_TEXT -> Textarea(onAnswerChange = { currentAnswer = it })
				}
			}
		}
	}

	dialogType?.let {
		when (it) {
			DialogType.QUESTION_NOT_COMPLETE -> QuestionNotCompleteDialog(
				onProceed = {
					saveAnswerForCurrentQuestion(questions[currentIndex.intValue], currentAnswer, candidateId, viewModel)
					if(currentIndex.intValue == questions.size - 1){
						dialogType = null
						showEndOfTestDialog = true
					} else {
						currentIndex.intValue++
						currentAnswer = getSavedAnswer(questions[currentIndex.intValue], candidateAnswers)
						dialogType = null
					}
				},
				onDismiss = { dialogType = null }
			)
			DialogType.ALL_QUESTIONS_NOT_COMPLETE -> AllQuestionNotCompleteDialog(
				onDismiss = { dialogType = null },
				onEndTest = {
					dialogType = null
					showEndOfTestDialog = true
				}
			)
			DialogType.ALL_QUESTIONS_COMPLETE -> AllQuestionCompleteDialog(
				onDismiss = { dialogType = null },
				onEndTest = {
					dialogType = null
					showEndOfTestDialog = true
				}
			)
		}
	}

	if (showEndOfTestDialog) {
		EndOfTestDialog(
			navController = navController,
			onDismiss = {
				showEndOfTestDialog = false
			}
		)
	}
}

// ================= Region Question Type =====================

/**
 * List of answers (3 types)
 */

@Composable
fun RadioButtonAnswer(options: List<String>, currentAnswer: String, onAnswerChange: (String) -> Unit){
	var selectedOption by remember(currentAnswer) { mutableStateOf(currentAnswer) }
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
fun CheckBoxAnswer(options: List<String>, currentAnswer: String, onAnswerChange: (String) -> Unit){
	val initialCheckedStates = remember(currentAnswer) {
		options.map { currentAnswer.split(", ").contains(it) }
	}
	val checkedStates = remember { mutableStateListOf(*initialCheckedStates.toTypedArray()) }

	Column(
		modifier = Modifier.fillMaxSize()
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

// ================= Region Question & Answer  =====================

/**
 * Handle display questions, save the data and get from room db
 */

fun saveAnswerForCurrentQuestion(
	question: Question,
	currentAnswer: String,
	candidateId : Long,
	viewModel: TestViewModel
) {
	viewModel.saveAnswer(question.id, currentAnswer, candidateId)
}

fun getSavedAnswer(
	question: Question,
	candidateAnswers: List<Answer>)
: String {
	return candidateAnswers.find { it.questionId == question.id }?.answerText ?: ""
}

enum class DialogType {
	QUESTION_NOT_COMPLETE,
	ALL_QUESTIONS_NOT_COMPLETE,
	ALL_QUESTIONS_COMPLETE
}

fun isQuestionComplete(question: Question, currentAnswer: String): Boolean {
	return when (question.questionType) {
		QuestionType.SINGLE_CHOICE -> currentAnswer.isNotEmpty()
		QuestionType.MULTIPLE_CHOICE -> currentAnswer.isNotEmpty()
		QuestionType.FREE_TEXT -> currentAnswer.isNotBlank()
	}
}

// ================= Region Dialog =====================

/**
 * Handle dialog if question is answered or not
 */

@Composable
private fun QuestionNotCompleteDialog(
	onProceed: () -> Unit,
	onDismiss: () -> Unit
) {
	UncompletedQuestionTheme {
		GeneralAlertDialog(
			titleResId = R.string.title_question_not_complete,
			messageResId = R.string.content_question_not_complete,
			onPositiveButtonClick = { onProceed() },
			onNegativeButtonClick = { onDismiss() }
		)
	}
}

@Composable
private fun AllQuestionCompleteDialog(
	onDismiss: () -> Unit,
	onEndTest: () -> Unit
){
	CompletedQuestionTheme {
		GeneralAlertDialog(
			titleResId = R.string.title_end_test,
			messageResId = R.string.content_end_test,
			onPositiveButtonClick = { onEndTest() },
			onNegativeButtonClick = { onDismiss() }
		)
	}
}

@Composable
private fun AllQuestionNotCompleteDialog(
	onDismiss: () -> Unit,
	onEndTest: () -> Unit
) {
	UncompletedQuestionTheme {
		GeneralAlertDialog(
			titleResId = R.string.title_question_not_complete,
			messageResId = R.string.content_all_question_not_complete,
			onPositiveButtonClick = { onEndTest() },
			onNegativeButtonClick = { onDismiss() }
		)
	}
}

@Composable
fun EndOfTestDialog(navController: NavController? = null,
					onDismiss: () -> Unit,){
	CompletedQuestionTheme {
		EndTestAlertDialog(
			titleResId = R.string.title_end_test,
			messageResId = R.string.content_end_test_final,
			onPositiveButtonClick = {
				navController?.navigate(Screen.Role)
			},
			onNegativeButtonClick = { onDismiss() }
		)
	}
}

// change bottom bar color
//val themeContent: @Composable (@Composable () -> Unit) -> Unit = if (countdownState.value < 60 * 1000) {
//		{ UncompletedQuestionTheme(it) }
//	} else {
//		{ DefaultTheme(it) }
//	}
package io.rapidz.assignment1.ui.test

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import io.rapidz.assignment1.ui.GeneralAlertDialog
import io.rapidz.assignment1.LocalNavController
import io.rapidz.assignment1.R
import io.rapidz.assignment1.Screen
import io.rapidz.assignment1.data.Question
import io.rapidz.assignment1.data.QuestionData.question
import io.rapidz.assignment1.data.QuestionType
import io.rapidz.assignment1.data.Role
import io.rapidz.assignment1.navigate
import io.rapidz.assignment1.ui.spacing_1
import io.rapidz.assignment1.ui.spacing_24
import io.rapidz.assignment1.ui.spacing_4
import io.rapidz.assignment1.ui.spacing_8
import io.rapidz.assignment1.ui.*
import io.rapidz.assignment1.utils.Constants
import io.rapidz.assignment1.viewmodel.NavigationDirection
import io.rapidz.assignment1.viewmodel.TestViewModel

@Composable
fun TestScreen(
	navController: NavController? = LocalNavController.current,
	viewModel : TestViewModel = hiltViewModel()
){
	val uiState by viewModel.uiState.collectAsState()
	val dialogState by viewModel.dialogState.collectAsState()
	val timer by viewModel.timer.collectAsState()

	val formattedTime = formatSecondsToTime(timer)
	val currentQuestion = uiState.questions.getOrNull(uiState.currentQuestionIndex)
	val selectedAnswer = currentQuestion?.let { uiState.answers[it.id]?.answerText ?: "" } ?: ""
	var isShowEndTestDialog by remember { mutableStateOf(false) }

	val isCompleted = when (currentQuestion?.questionType) {
		QuestionType.FREE_TEXT -> selectedAnswer.trim().isNotBlank()
		else -> selectedAnswer.isNotEmpty()
	}

	val themeWrapper: @Composable (@Composable () -> Unit) -> Unit = if (isCompleted) {
		{ content -> CompletedQuestionTheme(content) }
	} else {
		{ content -> UncompletedQuestionTheme(content) }
	}

	BackHandler {
		viewModel.checkAllQuestions()
	}

	themeWrapper{
		BottomAppBar(
			role = Role(Constants.Role.ROLE_CANDIDATE),
			timerText = formattedTime,
			showFloatBtn = true,
			onLeftDoubleArrowClick = { viewModel.goToFirstQuestion()},
			onLeftArrowClick = { viewModel.goToPreviousQuestion() },
			onRightArrowClick = { viewModel.goToNextQuestion() },
			onRightDoubleArrowClick = {viewModel.goToLastQuestion() },
			onFloatingButtonClick = { viewModel.checkAllQuestions() }
		){
			Column(
				modifier = Modifier
					.fillMaxSize()
					.padding(spacing_20)
			){
				currentQuestion?.let { question ->
					TextLabelTitle(
						text = "Question" + " ${question.id}",
						typographyStyle = AppTypography.titleLarge
					)

					Spacer(modifier = Modifier.height(spacing_20))

					TextLabelTitle(
						text = question.questionText
					)

					Spacer(modifier = Modifier.height(spacing_4))

					when (question.questionType){
						QuestionType.SINGLE_CHOICE -> RadioButtonAnswer(
							options = question.options,
							currentAnswer = selectedAnswer,
							onAnswerChange = { viewModel.saveAnswerTemporarily(question.id, it) }
						)

						QuestionType.MULTIPLE_CHOICE -> CheckBoxAnswer(
							options = question.options,
							currentAnswer = selectedAnswer,
							onAnswerChange = { viewModel.saveAnswerTemporarily(question.id, it) }
						)

						QuestionType.FREE_TEXT -> Textarea(
							initialText = selectedAnswer,
							onAnswerChange = { viewModel.saveAnswerTemporarily(question.id, it) }
						)
					}
				}
			}
		}
	}

	// Display dialog
	when (dialogState) {
		DialogType.QUESTION_NOT_COMPLETE -> QuestionNotCompleteDialog(
			onProceed = {
				viewModel.dismissDialog()
				when(viewModel.lastNavigation){
					NavigationDirection.NEXT -> viewModel.goToNextQuestion(true)
					NavigationDirection.PREVIOUS -> viewModel.goToPreviousQuestion(true)
					NavigationDirection.FIRST -> viewModel.goToFirstQuestion(true)
					NavigationDirection.LAST -> viewModel.goToLastQuestion(true)
					else -> {}
				}
			},
			onDismiss = { viewModel.dismissDialog() }
		)

		DialogType.ALL_QUESTIONS_NOT_COMPLETE -> AllQuestionNotCompleteDialog(
			onDismiss = { viewModel.dismissDialog() },
			onEndTest = {
				viewModel.dismissDialog()
				isShowEndTestDialog = true
			}
		)

		DialogType.ALL_QUESTIONS_COMPLETE -> AllQuestionCompleteDialog(
			onDismiss = { viewModel.dismissDialog() },
			onEndTest = {
				viewModel.dismissDialog()
				isShowEndTestDialog = true
			}
		)

		null -> {}
	}

	if (isShowEndTestDialog){
		EndOfTestDialog(
			navController = navController,
			onDismiss = { isShowEndTestDialog = false })
	}
}

// ================= Region Question Type =====================

/**
 * List of answers (3 types)
 */

@Composable
fun RadioButtonAnswer(options: List<String>, currentAnswer: String, onAnswerChange: ((String) -> Unit)? = null){
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
							onAnswerChange?.let { it(option) }
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
fun CheckBoxAnswer(options: List<String>, currentAnswer: String, onAnswerChange: ((String) -> Unit)? = null){
	val initialCheckedStates = remember(currentAnswer) {
		currentAnswer.split(",").map { it.trim() }.filter { it.isNotEmpty() }.toSet()
	}
	val checkedStates = remember { mutableStateMapOf<String, Boolean>() }

	LaunchedEffect(currentAnswer) {
		checkedStates.clear()
		options.forEach { option ->
			checkedStates[option] = initialCheckedStates.contains(option)
		}
	}

	Column(
		modifier = Modifier.fillMaxSize()
	) {
		options.forEach { option ->
			Row(verticalAlignment = Alignment.CenterVertically) {
				Checkbox(
					checked = checkedStates[option] ?: false,
					onCheckedChange = { isChecked ->
						checkedStates[option] = isChecked
						val selectedOptions = checkedStates.filter { it.value }.keys
						onAnswerChange?.let { it(selectedOptions.joinToString(", ")) }
					}
				)
				Text(text = option)
			}
		}
	}
}

@Composable
fun Textarea(
	initialText: String = "",
	readOnly: Boolean = false,
	onAnswerChange: (String) -> Unit = {}
) {
	var text by remember { mutableStateOf(initialText) }

	val screenHeight = LocalConfiguration.current.screenHeightDp.dp
	val dynamicHeight = screenHeight * 0.65f

	TextField(
		value = initialText,
		onValueChange = {
			if (!readOnly){
				text = it
				onAnswerChange(it)
			}
		},
		modifier = Modifier
			.fillMaxWidth()
			.heightIn(min = dynamicHeight)
			.padding(spacing_4)
			.border(width = spacing_1, color = Color.Black),
		readOnly = readOnly
	)
}

// ================= Region Dialog =====================

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
			msgResId = R.string.content_question_not_complete,
			positiveBtnLbl = R.string.dialog_yes,
			negativeBtnLbl = R.string.dialog_no,
			onDismissRequest = { onDismiss() },
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
			msgResId = R.string.content_end_test,
			positiveBtnLbl = R.string.dialog_yes,
			negativeBtnLbl = R.string.dialog_no,
			onDismissRequest = { onDismiss() },
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
			msgResId = R.string.content_all_question_not_complete,
			positiveBtnLbl = R.string.dialog_yes,
			negativeBtnLbl = R.string.dialog_no,
			onDismissRequest = { onDismiss() },
			onPositiveButtonClick = { onEndTest() },
			onNegativeButtonClick = { onDismiss() }
		)
	}
}

@Composable
fun EndOfTestDialog(navController: NavController? = null,
					onDismiss: () -> Unit){
	CompletedQuestionTheme {
		GeneralAlertDialog(
			titleResId = R.string.title_end_test,
			msgResId = R.string.content_end_test_final,
			positiveBtnLbl = R.string.dialog_yes,
			negativeBtnLbl = R.string.dialog_no,
			onDismissRequest = { onDismiss() },
			onPositiveButtonClick = {
				navController?.navigate(Screen.Role)
			},
			onNegativeButtonClick = { onDismiss() }
		)
	}
}

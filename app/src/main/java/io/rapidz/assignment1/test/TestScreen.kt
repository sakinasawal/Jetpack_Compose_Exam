package io.rapidz.assignment1.test

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
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
import io.rapidz.assignment1.formatSecondsToTime
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
import io.rapidz.assignment1.viewmodel.CandidateDataStoreViewModel
import io.rapidz.assignment1.viewmodel.CandidateDataStoreViewModelFactory
import io.rapidz.assignment1.viewmodel.TestViewModel
import io.rapidz.assignment1.viewmodel.TestViewModelFactory
import kotlinx.coroutines.delay

@Composable
fun TestScreen(
	navController: NavController? = null,
	candidateId: Long,
	usePreviousData: Boolean
){
	val context = LocalContext.current
	val database = remember { AppDatabase.getDatabase(context) }
	val repository = remember { TestRepository(database.answerDao()) }
	val viewModel: TestViewModel = viewModel(factory = TestViewModelFactory(repository))

	val questions = viewModel.questions
	val currentIndex = remember { mutableIntStateOf(0) }
	var currentAnswer by remember { mutableStateOf("") }
	val candidateAnswers by viewModel.getAnswersByCandidate(candidateId).collectAsState(initial = emptyList())

	val currentQuestion = questions[currentIndex.intValue]

	var dialogType by remember { mutableStateOf<DialogType?>(null) }
	var showEndOfTestDialog by remember { mutableStateOf(false) }
	var showQuestionNotCompleteDialog by remember { mutableStateOf(false) }

	val candidateDataStoreViewModel: CandidateDataStoreViewModel = viewModel(
		factory = CandidateDataStoreViewModelFactory(context)
	)
	val testTimeLimit by candidateDataStoreViewModel.testTimeLimit.collectAsState(initial = 0)
	var remainingTime by rememberSaveable { mutableStateOf(0)}
	var timerStarted by remember { mutableStateOf(false) }
	val formattedTimer = formatSecondsToTime(remainingTime)
	val initialTime = testTimeLimit * 60

	val isCompleted = candidateAnswers.any { it.questionId == currentQuestion.id && it.answerText.isNotEmpty() }

	LaunchedEffect(candidateId, testTimeLimit) {
		if (testTimeLimit > 0) { // Ensure testTimeLimit is loaded from DataStore
			val savedRemainingTime = viewModel.getRemainingTimeForCandidate(candidateId)
			remainingTime = if (savedRemainingTime > 0) {
				savedRemainingTime // Resume from saved time
			} else {
				initialTime // Use test time from DataStore
			}
			timerStarted = true
		}
	}

	LaunchedEffect(remainingTime, timerStarted) {
		if (timerStarted && remainingTime > 0) {
			while (remainingTime > 0) {
				delay(1000L)
				remainingTime--
			}
		}
	}

	LaunchedEffect(candidateId, usePreviousData) {
		if (usePreviousData) {
			val firstUnansweredIndex = questions.indexOfFirst { question ->
				candidateAnswers.none { it.questionId == question.id && it.answerText.isNotEmpty() }
			}

			currentIndex.intValue = if (firstUnansweredIndex != -1) firstUnansweredIndex else 0
			currentAnswer = getSavedAnswer(currentQuestion, candidateAnswers)
		} else {
			viewModel.clearAnswersForCandidate(candidateId)
			currentIndex.intValue = 0
			currentAnswer = ""
		}
	}

	val themeWrapper: @Composable (@Composable () -> Unit) -> Unit = if (isCompleted) {
		{ content -> CompletedQuestionTheme(content) }
	} else {
		{ content -> UncompletedQuestionTheme(content) }
	}

	themeWrapper {
		BottomAppBar(
			timer = formattedTimer,
			remainingTime = remainingTime,
			onLeftArrowClick = {
				if (!isQuestionComplete(currentQuestion, currentAnswer)){
					dialogType = DialogType.QUESTION_NOT_COMPLETE
				} else if (currentIndex.intValue > 0) {
					saveAnswerForCurrentQuestion(currentQuestion, currentAnswer, candidateId, remainingTime, initialTime, viewModel)
					currentIndex.intValue--
					currentAnswer = getSavedAnswer(currentQuestion, candidateAnswers)
				}
			},
			onRightArrowClick = {
				if (!isQuestionComplete(currentQuestion, currentAnswer)) {
					dialogType = DialogType.QUESTION_NOT_COMPLETE
				} else {
					saveAnswerForCurrentQuestion(currentQuestion, currentAnswer, candidateId, remainingTime, initialTime, viewModel)
					if (currentIndex.intValue == questions.size - 1) {
						val allQuestionsComplete = questions.all { question ->
							if (question.id == currentQuestion.id){
								currentAnswer.isNotBlank() && currentAnswer.isNotEmpty()
							} else {
								candidateAnswers.any { it.questionId == question.id && it.answerText.isNotEmpty()}
							}
						}
						dialogType = if (allQuestionsComplete) {
							DialogType.ALL_QUESTIONS_COMPLETE
						} else {
							DialogType.ALL_QUESTIONS_NOT_COMPLETE
						}
					} else {
						currentIndex.intValue++
						currentAnswer = getSavedAnswer(currentQuestion, candidateAnswers)
					}
				}
			},
			onLeftDoubleArrowClick = {
				currentIndex.intValue = 0
				currentAnswer = getSavedAnswer(questions[0], candidateAnswers)
			},
			onRightDoubleArrowClick = {
				saveAnswerForCurrentQuestion(currentQuestion, currentAnswer, candidateId, remainingTime, initialTime, viewModel)
				currentIndex.intValue = questions.size - 1
				currentAnswer = getSavedAnswer(questions.last(), candidateAnswers)
			},
			onFloatingButtonClick = {
				if (!isQuestionComplete(currentQuestion, currentAnswer)) {
					dialogType = DialogType.QUESTION_NOT_COMPLETE
				} else {
					saveAnswerForCurrentQuestion(currentQuestion, currentAnswer, candidateId, remainingTime, initialTime, viewModel)
					if (currentIndex.intValue == questions.size - 1) {
						val allQuestionsComplete = questions.all { question ->
							if (question.id == currentQuestion.id){
								currentAnswer.isNotBlank() && currentAnswer.isNotEmpty()
							} else {
								candidateAnswers.any { it.questionId == question.id && it.answerText.isNotEmpty()}
							}
						}
						dialogType = if (allQuestionsComplete) {
							DialogType.ALL_QUESTIONS_COMPLETE
						} else {
							DialogType.ALL_QUESTIONS_NOT_COMPLETE
						}
					} else {
						currentIndex.intValue++
						currentAnswer = getSavedAnswer(currentQuestion, candidateAnswers)
					}
				}
			}
		){
			Column(
				modifier = Modifier
					.fillMaxSize()
					.background(color = md_theme_default_primaryContainer)
					.padding(spacing_20)
			){
				val question = currentQuestion

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
					QuestionType.FREE_TEXT -> Textarea(
						initialText = currentAnswer,
						onAnswerChange = { currentAnswer = it })
				}
			}
		}
	}

	BackHandler {
		if (!isQuestionComplete(currentQuestion, currentAnswer)) {
			showQuestionNotCompleteDialog = true
		} else {
			// Save the answer for the current question
			saveAnswerForCurrentQuestion(
				currentQuestion,
				currentAnswer,
				candidateId,
				remainingTime,
				initialTime,
				viewModel
			)
			dialogType = null
			showEndOfTestDialog = true
		}
	}

	if (showQuestionNotCompleteDialog) {
		QuestionNotCompleteDialog(
			onProceed = {
				// Save the current answer (whether answered or not)
				saveAnswerForCurrentQuestion(
					currentQuestion,
					currentAnswer,
					candidateId,
					remainingTime,
					initialTime,
					viewModel
				)
				// Do not increment the index, so stay on the current question
				dialogType = null
				showEndOfTestDialog = true
				showQuestionNotCompleteDialog = false // Hide the dialog
			},
			onDismiss = {
				// Dismiss the dialog and keep the user on the current question
				showQuestionNotCompleteDialog = false
			}
		)
	}

	fun stopTimer(){
		timerStarted = false
		remainingTime = 0
	}

	dialogType?.let {
		when (it) {
			DialogType.QUESTION_NOT_COMPLETE -> QuestionNotCompleteDialog(
				onProceed = {
					saveAnswerForCurrentQuestion(currentQuestion, currentAnswer, candidateId, remainingTime, initialTime, viewModel)
					if(currentIndex.intValue == questions.size - 1){
						dialogType = DialogType.ALL_QUESTIONS_NOT_COMPLETE
					}
					else {
						currentIndex.intValue++
						currentAnswer = getSavedAnswer(currentQuestion, candidateAnswers)
						dialogType = null
					}
				},
				onDismiss = { dialogType = null }
			)
			DialogType.ALL_QUESTIONS_NOT_COMPLETE -> AllQuestionNotCompleteDialog(
				onDismiss = { dialogType = null },
				onEndTest = {
					stopTimer()
					dialogType = null
					showEndOfTestDialog = true
				}
			)
			DialogType.ALL_QUESTIONS_COMPLETE -> AllQuestionCompleteDialog(
				onDismiss = { dialogType = null },
				onEndTest = {
					stopTimer()
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
fun Textarea(
	initialText: String = "",
	readOnly: Boolean = false,
	onAnswerChange: (String) -> Unit = {}
) {
	var text by remember { mutableStateOf(initialText) }

	val screenHeight = LocalConfiguration.current.screenHeightDp.dp
	val dynamicHeight = screenHeight * 0.65f

	val focusManager = LocalFocusManager.current
	val keyboardController = LocalSoftwareKeyboardController.current

	TextField(
		value = text,
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
			.border(width = spacing_1, color = Color.Black)
			.pointerInput(Unit) {
				detectTapGestures(onTap = {
					keyboardController?.hide()
					focusManager.clearFocus() })
			},
		readOnly = readOnly
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
	remainingTime: Int,
	initialTime: Int,
	viewModel: TestViewModel,
) {
	viewModel.saveAnswer(question.id, currentAnswer, candidateId, remainingTime, initialTime, question.questionType, question.defaultAnswer)
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
		QuestionType.FREE_TEXT -> currentAnswer.isNotBlank() && currentAnswer.isNotEmpty()
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
					onDismiss: () -> Unit){
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

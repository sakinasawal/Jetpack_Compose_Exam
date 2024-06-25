package io.rapidz.assignment1.test

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.*
import androidx.navigation.NavController
import io.rapidz.assignment1.EndTestAlertDialog
import io.rapidz.assignment1.GeneralAlertDialog
import io.rapidz.assignment1.R
import io.rapidz.assignment1.Screen
import io.rapidz.assignment1.navigate
import io.rapidz.assignment1.ui.DefaultTheme
import io.rapidz.assignment1.ui.*
import io.rapidz.assignment1.ui.RadioButtonAnswer
import kotlinx.coroutines.delay

@Preview
@Composable
fun TestScreenBottomNav(
	navController: NavController? = null
){
	val totalTimeMillis = 60 * 1000L // 1 minute in milliseconds
	val countdownState = remember { mutableStateOf(totalTimeMillis) }
	val currentQuestionIndex = remember { mutableStateOf(0) }

	LaunchedEffect(Unit) {
		while (countdownState.value > 0) {
			delay(1000) // Wait for 1 second
			countdownState.value -= 1000 // Decrement by 1 second
		}
	}

	SideEffect {
		// Example of saving an answer
//		val answer = Answer(userId = userId, questionId = currentQuestionIndex.value, answer = "Example Answer")
//		viewModel.insertAnswer(answer)
	}

	// List of questions
	val questions = listOf<@Composable () -> Unit>(
		{ Question1() },
		{ Question2() },
		{ Question3() }
	)

	DefaultTheme {
		BottomNavBar(
			countdownMillis = countdownState.value,
			currentQuestionIndex = currentQuestionIndex.value,
			totalQuestions = questions.size,
			onLeftDoubleArrowClick = { currentQuestionIndex.value = 0 },
			onLeftArrowClick = { if (currentQuestionIndex.value > 0) currentQuestionIndex.value-- },
			onRightArrowClick = { if (currentQuestionIndex.value < questions.size - 1) currentQuestionIndex.value++ },
			onRightDoubleArrowClick = { currentQuestionIndex.value = questions.size - 1 },
			onFloatingButtonClick = { /*show alert dialog*/ }
		) {
			questions[currentQuestionIndex.value]()
		}
	}
}

@Preview
@Composable
private fun QuestionNotCompleteDialog(navController: NavController? = null){
	UncompletedQuestionTheme {
		GeneralAlertDialog(
			titleResId = R.string.title_question_not_complete,
			messageResId = R.string.content_question_not_complete,
			onPositiveButtonClick = {
				navController!!.navigate(Screen.Candidate)
//				closeDialog()
			},
			onNegativeButtonClick = {
//				closeDialog()
			}
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
			onPositiveButtonClick = {
				navController!!.navigate(Screen.Candidate)
//				closeDialog()
			},
			onNegativeButtonClick = {
//				closeDialog()
			}
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
				navController!!.navigate(Screen.Candidate)
//				closeDialog()
			}
		)
	}
}
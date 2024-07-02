package io.rapidz.assignment1.test

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import io.rapidz.assignment1.EndTestAlertDialog
import io.rapidz.assignment1.GeneralAlertDialog
import io.rapidz.assignment1.R
import io.rapidz.assignment1.repository.CandidateRepository
import io.rapidz.assignment1.storage.AppDatabase
import io.rapidz.assignment1.storage.DataStoreManager
import io.rapidz.assignment1.ui.DefaultTheme
import io.rapidz.assignment1.ui.*
import io.rapidz.assignment1.viewmodel.CandidateViewModel
import io.rapidz.assignment1.viewmodel.CandidateViewModelFactory
import kotlinx.coroutines.delay

@Preview
@Composable
fun TestScreenBottomNav(
	navController: NavController? = null
){
	val context = LocalContext.current
	val database = remember { AppDatabase.getDatabase(context) }
	val candidateRepository = remember { CandidateRepository(answerDao = database.answerDao()) }
	val viewModel : CandidateViewModel = viewModel(factory = CandidateViewModelFactory(candidateRepository))

	val totalTimeMillis = 2*60 * 1000L // 5 minute in milliseconds
	val countdownState = remember { mutableStateOf(totalTimeMillis) }
	val currentQuestionIndex = remember { mutableStateOf(0) }
	val answerText = remember { mutableStateOf("") }

	LaunchedEffect(Unit) {
		while (countdownState.value > 0) {
			delay(1000) // Wait for 1 second
			countdownState.value -= 1000 // Decrement by 1 second
		}


	}

	SideEffect {
		// Saving an answer
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
			onPositiveButtonClick ={},
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
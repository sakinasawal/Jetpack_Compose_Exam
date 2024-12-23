package io.rapidz.assignment1.test

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import io.rapidz.assignment1.data.QuestionType
import io.rapidz.assignment1.repository.CandidateRepository
import io.rapidz.assignment1.repository.TestRepository
import io.rapidz.assignment1.spacing_10
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

	DefaultTheme {
		BottomAppBar(
			onLeftArrowClick = {
				if (currentIndex.intValue > 0) {
				currentIndex.intValue--
			}},
			onRightArrowClick = {
				if (currentIndex.intValue < questions.size - 1) {
					currentIndex.intValue++
				}
			},
			onLeftDoubleArrowClick = {
				currentIndex.intValue = 0
				Unit
			},
			onRightDoubleArrowClick = {
				if (currentIndex.intValue < viewModel.questions.size - 1) {
					currentIndex.intValue += 1
				}
			}
		){
			Column(modifier = Modifier.fillMaxSize()){
				val question = questions[currentIndex.intValue]
					when (question.questionType) {
						QuestionType.SINGLE_CHOICE -> RadioButtonAnswer(question.id, viewModel)
						QuestionType.MULTIPLE_CHOICE -> CheckBoxAnswer(question.id, viewModel)
						QuestionType.FREE_TEXT -> Textarea(question.id, viewModel)
					}
				}
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
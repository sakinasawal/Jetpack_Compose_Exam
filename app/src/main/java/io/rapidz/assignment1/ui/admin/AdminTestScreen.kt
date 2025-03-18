package io.rapidz.assignment1.ui.admin

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import io.rapidz.assignment1.viewmodel.AdminViewModel
import io.rapidz.assignment1.viewmodel.TestViewModel
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Dangerous
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import io.rapidz.assignment1.data.QuestionType
import io.rapidz.assignment1.data.Role
import io.rapidz.assignment1.ui.AdminTheme
import io.rapidz.assignment1.ui.AppTypography
import io.rapidz.assignment1.ui.TextLabelTitle
import io.rapidz.assignment1.ui.md_theme_admin_error
import io.rapidz.assignment1.ui.spacing_20
import io.rapidz.assignment1.ui.spacing_4
import io.rapidz.assignment1.ui.test.BottomAppBar
import io.rapidz.assignment1.ui.test.CheckBoxAnswer
import io.rapidz.assignment1.ui.test.RadioButtonAnswer
import io.rapidz.assignment1.ui.test.Textarea
import io.rapidz.assignment1.utils.Constants

@Composable
fun AdminTestScreen(viewModel : AdminViewModel = hiltViewModel()) {

	val uiState by viewModel.uiState.collectAsState()
	val currentQuestion = uiState.questions.getOrNull(uiState.currentQuestionIndex)
	val candidateAnswer = currentQuestion?.let { uiState.answers[it.id]?.answerText.orEmpty() }
	val isCorrectAnswer = currentQuestion?.let { viewModel.isAnswerCorrect(it, candidateAnswer) } == true
	val isFreeText = currentQuestion?.questionType == QuestionType.FREE_TEXT

	AdminTheme {
		BottomAppBar(
			role = Role(Constants.Role.ROLE_ADMIN),
			showDoneIcon = if(isFreeText) true else isCorrectAnswer,
			showCloseIcon = if (isFreeText) true else !isCorrectAnswer,
			closeIcon = if (isFreeText) Icons.Default.Close else Icons.Default.Dangerous,
			closeIconColor = if (isFreeText) Color.Black else md_theme_admin_error,
			doneIconColor = if (isFreeText) Color.Black else Color(0xFF018786),
			showFloatBtn = false,
			onLeftDoubleArrowClick = { viewModel.goToFirstQuestion() },
			onLeftArrowClick = { viewModel.goToPreviousQuestion() },
			onRightArrowClick = { viewModel.goToNextQuestion() },
			onRightDoubleArrowClick = { viewModel.goToLastQuestion() },
		){
			Column(
				modifier = Modifier
					.fillMaxSize()
					.padding(spacing_20)
			){
				currentQuestion?.let { question ->

					TextLabelTitle(
						text = "Question ${question.id}",
						typographyStyle = AppTypography.titleLarge
					)

					Spacer(modifier = Modifier.height(spacing_20))

					TextLabelTitle(text = question.questionText)

					Spacer(modifier = Modifier.height(spacing_4))

					candidateAnswer?.let {
						when(question.questionType){
							QuestionType.SINGLE_CHOICE -> RadioButtonAnswer(
									options = question.options,
									currentAnswer = it
							)

							QuestionType.MULTIPLE_CHOICE -> CheckBoxAnswer(
								options = question.options,
								currentAnswer = it
							)

							QuestionType.FREE_TEXT -> Textarea(
								initialText = it,
								readOnly = true
							)
						}
					}
				}
			}
		}
	}
}
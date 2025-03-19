package io.rapidz.assignment1.ui.admin

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import io.rapidz.assignment1.viewmodel.AdminViewModel
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Dangerous
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import io.rapidz.assignment1.data.QuestionType
import io.rapidz.assignment1.data.Role
import io.rapidz.assignment1.ui.AdminTheme
import io.rapidz.assignment1.ui.AppTypography
import io.rapidz.assignment1.ui.BottomAppBar
import io.rapidz.assignment1.ui.TextLabelTitle
import io.rapidz.assignment1.ui.md_theme_admin_error
import io.rapidz.assignment1.ui.spacing_20
import io.rapidz.assignment1.ui.spacing_4
import io.rapidz.assignment1.ui.test.CheckBoxAnswer
import io.rapidz.assignment1.ui.test.RadioButtonAnswer
import io.rapidz.assignment1.ui.test.Textarea
import io.rapidz.assignment1.utils.Constants

@Composable
fun AdminTestScreen(viewModel : AdminViewModel = hiltViewModel()) {

	val uiState by viewModel.uiState.collectAsState()
	val freeTextScoreUi by viewModel.freeTextScores.collectAsState()

	val currentQuestion = uiState.questions.getOrNull(uiState.currentQuestionIndex)
	val candidateAnswer = currentQuestion?.let { uiState.answers[it.id]?.answerText.orEmpty() }
	val questionType = currentQuestion?.questionType

	val isFreeText = questionType == QuestionType.FREE_TEXT
	val freeTextScore = freeTextScoreUi[currentQuestion?.id]
	val bothIconShown = isFreeText && freeTextScore == null

	val isCorrectAnswer = when(questionType){
		QuestionType.SINGLE_CHOICE, QuestionType.MULTIPLE_CHOICE -> {
			currentQuestion.let { viewModel.isAnswerCorrect(it, candidateAnswer) }
		}
		QuestionType.FREE_TEXT -> freeTextScore == true
		else -> false
	}

	AdminTheme {
		BottomAppBar(
			role = Role(Constants.Role.ROLE_ADMIN),
			showDoneIcon = when{
				isFreeText && freeTextScore == null -> true
				isFreeText && freeTextScore == true -> true
				isFreeText -> false
				else -> isCorrectAnswer
			},
			showCloseIcon = when{
				isFreeText && freeTextScore == null -> true
				isFreeText && freeTextScore == false -> true
				isFreeText -> false
				else -> !isCorrectAnswer
			},
			closeIcon = if (isFreeText) Icons.Default.Close else Icons.Default.Dangerous,
			closeIconColor = when {
				bothIconShown -> Color.Black
				isFreeText && freeTextScore == false -> md_theme_admin_error // Incorrect answer
				else -> md_theme_admin_error
			},
			doneIconColor = when {
				bothIconShown -> Color.Black
				isFreeText && freeTextScore == true -> Color(0xFF018786) // Correct answer
				else -> Color(0xFF018786)
			},
			onDoneClick = if (isFreeText) { { currentQuestion?.id?.let { viewModel.scoreFreeText(it, true) } } } else null,
			onCloseClick = if (isFreeText) { { currentQuestion?.id?.let { viewModel.scoreFreeText(it, false) } } } else null,
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
package io.rapidz.assignment1.admin

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material3.*
import androidx.lifecycle.viewmodel.compose.viewModel
import io.rapidz.assignment1.InputTextField
import io.rapidz.assignment1.R
import io.rapidz.assignment1.TextLabel
import io.rapidz.assignment1.*
import io.rapidz.assignment1.data.Candidate
import io.rapidz.assignment1.repository.CandidateRepository
import io.rapidz.assignment1.storage.AppDatabase
import io.rapidz.assignment1.ui.AdminTheme
import io.rapidz.assignment1.ui.AppTypography
import io.rapidz.assignment1.viewmodel.CandidateViewModel
import io.rapidz.assignment1.viewmodel.CandidateViewModelFactory
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.navigation.NavController
import io.rapidz.assignment1.repository.TestRepository
import io.rapidz.assignment1.ui.*
import io.rapidz.assignment1.viewmodel.TestViewModel
import io.rapidz.assignment1.viewmodel.TestViewModelFactory
import kotlinx.coroutines.delay

@Composable
fun AdminHomeScreen(navController : NavController ?= null) {

	val context = LocalContext.current
	val database = remember { AppDatabase.getDatabase(context) }

	val candidateRepository = remember { CandidateRepository(database.candidateDao()) }
	val viewModel: CandidateViewModel = viewModel(factory = CandidateViewModelFactory(candidateRepository))
	var candidates by remember { mutableStateOf<List<Candidate>>(emptyList()) }

	val answerRepository = remember { TestRepository(database.answerDao()) }
	val answerViewModel: TestViewModel = viewModel(factory = TestViewModelFactory(answerRepository))
	var totalScore by remember { mutableStateOf("?") }

	var displayedCandidates by remember { mutableStateOf<List<Candidate>>(emptyList()) }
	var searchQuery by remember { mutableStateOf("") }

	val focusManager = LocalFocusManager.current
	val keyboardController = LocalSoftwareKeyboardController.current

	val timeLimit = stringResource(id = R.string.time_limit_admin)

	LaunchedEffect(Unit) {
		viewModel.getAllCandidates { fetchedCandidates ->
			candidates = fetchedCandidates
			displayedCandidates = fetchedCandidates

			val scores = fetchedCandidates.map { candidate ->
				answerViewModel.getCandidateScore(candidate.id)
			}

			totalScore = if (scores.contains("?")) {
				"?"
			} else {
				scores.filterIsInstance<Int>().sum().toString()
			}
		}
	}

	LaunchedEffect(searchQuery) {
		if (searchQuery.isNotEmpty()) {
			delay(2000)
			displayedCandidates = candidates.filter { candidate ->
				candidate.name.contains(searchQuery, ignoreCase = true)
			}
		} else {
			displayedCandidates = candidates
		}
	}

	AdminTheme {
		Column(
			modifier = Modifier
				.fillMaxSize()
				.background(color = Color.White)
				.padding(all = spacing_20)
				.pointerInput(Unit) {
					detectTapGestures(onTap = {
						keyboardController?.hide()
						focusManager.clearFocus()
					})
				},
			verticalArrangement = Arrangement.spacedBy(spacing_20),
			horizontalAlignment = Alignment.Start
		){
			TextLabel(
				text = R.string.admin_home,
				typographyStyle = AppTypography.titleLarge
			)

			InputTextFieldAdmin(
				value = "30",
				onValueChange = {},
				label = timeLimit
			)

			TextLabel(
				text = R.string.taken_tests,
				modifier = Modifier.padding(top = spacing_20)
			)

			InputTextField(
				value = searchQuery,
				onValueChange = { newQuery ->
					searchQuery = newQuery
				},
				placeholder = stringResource(id = R.string.search)
			)

			TableHeader(
				headers = listOf("Time", "Name", "Score"),
				weights = listOf(1f, 2f, 1f)
			)

			LazyColumn(
				modifier = Modifier
					.fillMaxWidth(),
				verticalArrangement = Arrangement.spacedBy(spacing_8)
			) {
				items(displayedCandidates) { candidate ->
					val candidateScore = answerViewModel.getCandidateScore(candidate.id)
					TableRow(
						time = "000m",
						name = candidate.name,
						score = candidateScore,
						onClick = {
							navController?.navigate("AdminTest/${candidate.id}")
						}
					)
				}
			}
		}
	}
}

@SuppressLint("ModifierParameter")
@Composable
fun TableHeader(
	headers : List<String>,
	weights : List<Float>,
	backgroundColor : Color = md_theme_admin_primaryContainer,
	textColor : Color = Color.Black,
	modifier : Modifier = Modifier
){
	Row(
		modifier = modifier
			.fillMaxWidth()
			.background(color = backgroundColor)
			.padding(vertical = spacing_8, horizontal = spacing_16),
		horizontalArrangement = Arrangement.SpaceBetween
	) {
		headers.forEachIndexed { index, header ->
			Text(
				text = header,
				modifier = Modifier.weight(weights.getOrElse(index) { 1f }),
				color = textColor
			)
		}
	}
}

@Composable
fun TableRow(
	time: String,
	name: String,
	score: String,
	onClick: () -> Unit
){
	Row(
		modifier = Modifier
			.fillMaxWidth()
			.background(color = md_theme_admin_tertiaryContainer)
			.clickable(onClick = onClick)
			.padding(vertical = spacing_8, horizontal = spacing_16),
		horizontalArrangement = Arrangement.SpaceBetween
	) {
		Text(text = time, modifier = Modifier.weight(1f))
		Text(text = name, modifier = Modifier.weight(2f))
		Text(text = score, modifier = Modifier.weight(1f))
	}
}


@Preview
@Composable
private fun AdminScreenPreview(){
	AdminHomeScreen()
}
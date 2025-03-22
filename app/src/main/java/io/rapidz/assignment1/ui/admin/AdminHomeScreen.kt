package io.rapidz.assignment1.ui.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.input.pointer.pointerInput
import io.rapidz.assignment1.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import io.rapidz.assignment1.R
import io.rapidz.assignment1.data.Candidate
import io.rapidz.assignment1.data.CandidateWithScore
import io.rapidz.assignment1.ui.*
import io.rapidz.assignment1.utils.Constants
import io.rapidz.assignment1.viewmodel.AdminViewModel

@Composable
fun AdminHome(navController : NavController = LocalNavController.current,
			  viewModel : AdminViewModel = hiltViewModel()
) {
	val timeLimit by viewModel.timeLimit.collectAsState()
	val candidatesWithScores by viewModel.candidatesWithScores.collectAsState()
	val isGifVisible by viewModel.isGifVisible.collectAsState()


	LaunchedEffect(Unit) {
		viewModel.loadCandidate()
	}

	AdminHomeScreen(
		navController = navController,
		viewModel = viewModel,
		timeLimit = timeLimit,
		isGifVisible = isGifVisible,
		candidatesWithScores = candidatesWithScores
	)
}

@Composable
fun AdminHomeScreen(
	navController: NavController? = null,
	viewModel : AdminViewModel? = null,
	timeLimit : Long = 30L,
	isGifVisible : Boolean = false,
	candidatesWithScores : List<CandidateWithScore> = emptyList()
){
	val context = LocalContext.current
	val focusManager = LocalFocusManager.current
	val keyboardController = LocalSoftwareKeyboardController.current

	var searchText by remember { mutableStateOf("") }

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

			InputTextFieldTime(
				value = timeLimit,
				onValueChange = { viewModel?.setTimeLimit(it)},
				label = stringResource(id = R.string.time_limit_admin),
				placeholder = stringResource(id = R.string.defaultTime)
			)

			TextLabel(
				text = R.string.taken_tests,
				modifier = Modifier.padding(top = spacing_20)
			)

			if(isGifVisible){
				GifImage(context, Constants.URL.URL_GIF)
			} else {
				InputTextSearch(
					value = searchText,
					onValueChange = {
						searchText = it
						viewModel?.updateSearchQuery(it)
					},
					label = stringResource(R.string.search),
					placeholder = stringResource(id = R.string.search_name)
				)

				TableHeader(
					headers = listOf("Time", "Name", "Score"),
					weights = listOf(1f, 2f, 1f)
				)

				LazyColumn(
					modifier = Modifier.fillMaxWidth(),
					verticalArrangement = Arrangement.spacedBy(spacing_8)
				) {
					items(candidatesWithScores) { candidateWithScore ->
						TableRow(
							time = formatSecondsToTime(candidateWithScore.totalTimeSpent),
							name = candidateWithScore.candidate.name,
							score = candidateWithScore.totalScore?.toString() ?: "?",
							onClick = { navController?.navigate("${Route.ADMIN_TEST}/${candidateWithScore.candidate.id}") }
						)
					}
				}
			}
		}
	}
}

@Preview
@Composable
private fun AdminScreenPreview(){
	AdminHomeScreen(
		candidatesWithScores = listOf(
			CandidateWithScore(
				candidate = Candidate(id = 1, name = "Sakina", emailAddress = ""),
				totalScore = 85,
				totalTimeSpent = 120
			),
			CandidateWithScore(
				candidate = Candidate(id = 1, name = "Najihah", emailAddress = ""),
				totalScore = 85,
				totalTimeSpent = 100
			)
		)
	)
}


package io.rapidz.assignment1.ui.admin

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material3.*
import io.rapidz.assignment1.*
import androidx.compose.ui.layout.ContentScale
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.request.ImageRequest
import io.rapidz.assignment1.ui.*

@Composable
fun AdminHomeScreen(navController : NavController ?= LocalNavController.current) {

//	val context = LocalContext.current
//	val database = remember { AppDatabase.getDatabase(context) }
//
//	val candidateRepository = remember { CandidateRepository(database.candidateDao()) }
//	val viewModel: CandidateViewModel = viewModel(factory = CandidateViewModelFactory(candidateRepository))
//	var candidates by remember { mutableStateOf<List<Candidate>>(emptyList()) }
//
//	val candidateDataStoreViewModel: CandidateDataStoreViewModel = viewModel(
//		factory = CandidateDataStoreViewModelFactory(context)
//	)
//
//	val answerRepository = remember { TestRepository(database.answerDao()) }
//	val answerViewModel: TestViewModel = viewModel(factory = TestViewModelFactory(answerRepository))
//	var totalScore by remember { mutableStateOf("?") }
//
//	var showGif by remember { mutableStateOf(true) }
//	var displayedCandidates by remember { mutableStateOf<List<Candidate>>(emptyList()) }
//	var searchQuery by remember { mutableStateOf("") }
//
//	val savedTimeLimit by candidateDataStoreViewModel.testTimeLimit.collectAsState(initial = 0)
//	var testTimeLimit by remember { mutableStateOf(savedTimeLimit) }
//
//	val focusManager = LocalFocusManager.current
//	val keyboardController = LocalSoftwareKeyboardController.current
//
//	val timeLimit = stringResource(id = R.string.time_limit_admin)
//
//	LaunchedEffect(Unit) {
//		delay(2000)
//		showGif = false
//
//		viewModel.getAllCandidates { fetchedCandidates ->
//			candidates = fetchedCandidates
//			displayedCandidates = fetchedCandidates
//
//			val scores = fetchedCandidates.map { candidate ->
//				answerViewModel.getCandidateScore(candidate.id)
//			}
//
//			totalScore = if (scores.contains("?")) {
//				"?"
//			} else {
//				scores.filterIsInstance<Int>().sum().toString()
//			}
//		}
//	}
//
//	LaunchedEffect(savedTimeLimit) {
//		if (testTimeLimit != savedTimeLimit) {
//			testTimeLimit = savedTimeLimit
//		}
//	}
//
//	LaunchedEffect(testTimeLimit) {
//		snapshotFlow { testTimeLimit }
//			.collect { newTimeLimit ->
//				candidateDataStoreViewModel.saveTestTimeLimit(context, newTimeLimit)
//			}
//	}
//
//	LaunchedEffect(searchQuery) {
//		if (searchQuery.isNotEmpty()) {
//			delay(2000)
//			displayedCandidates = candidates.filter { candidate ->
//				candidate.name.contains(searchQuery, ignoreCase = true)
//			}
//		} else {
//			displayedCandidates = candidates
//		}
//	}
//
//	AdminTheme {
//		Column(
//			modifier = Modifier
//				.fillMaxSize()
//				.background(color = Color.White)
//				.padding(all = spacing_20)
//				.pointerInput(Unit) {
//					detectTapGestures(onTap = {
//						keyboardController?.hide()
//						focusManager.clearFocus()
//					})
//				},
//			verticalArrangement = Arrangement.spacedBy(spacing_20),
//			horizontalAlignment = Alignment.Start
//		){
//			TextLabel(
//				text = R.string.admin_home,
//				typographyStyle = AppTypography.titleLarge
//			)
//
//			InputTextFieldAdmin(
//				value = testTimeLimit,
//				onValueChange = { newValue ->
//					testTimeLimit = newValue
//				},
//				label = timeLimit,
//				placeholder = stringResource(id = R.string.defaultTime)
//			)
//
//			TextLabel(
//				text = R.string.taken_tests,
//				modifier = Modifier.padding(top = spacing_20)
//			)
//
//			if (showGif) {
//				GifImage(context, URL)
//			} else {
//				InputTextField(
//					value = searchQuery,
//					onValueChange = { newQuery ->
//						searchQuery = newQuery
//					},
//					placeholder = stringResource(id = R.string.search)
//				)
//
//				TableHeader(
//					headers = listOf("Time", "Name", "Score"),
//					weights = listOf(1f, 2f, 1f)
//				)
//
//				LazyColumn(
//					modifier = Modifier
//						.fillMaxWidth(),
//					verticalArrangement = Arrangement.spacedBy(spacing_8)
//				) {
//					items(displayedCandidates) { candidate ->
//						val candidateScore = answerViewModel.getCandidateScore(candidate.id)
//
//						val totalTimeTaken by answerViewModel.getTotalTimeTaken(candidate.id).collectAsState()
//						val formattedTimer = formatSecondsToTime(totalTimeTaken)
//
//						TableRow(
//							time = formattedTimer,
//							name = candidate.name,
//							score = candidateScore,
//							onClick = {
//								navController?.navigate("AdminTest/${candidate.id}")
//							}
//						)
//					}
//				}
//			}
//		}
//	}
}

@Composable
fun GifImage(context : Context, url : String) {
	AsyncImage(
		model = ImageRequest.Builder(context)
			.data(url)
			.crossfade(true)
			.decoderFactory(GifDecoder.Factory())
			.build(),
		contentDescription = null,
		modifier = Modifier
			.fillMaxSize(),
		contentScale = ContentScale.Crop
	)
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
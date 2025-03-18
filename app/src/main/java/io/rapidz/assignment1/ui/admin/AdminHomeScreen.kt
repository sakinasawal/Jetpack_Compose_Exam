package io.rapidz.assignment1.ui.admin

import android.annotation.SuppressLint
import android.content.Context
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
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.input.pointer.pointerInput
import io.rapidz.assignment1.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.request.ImageRequest
import io.rapidz.assignment1.R
import io.rapidz.assignment1.ui.*
import io.rapidz.assignment1.utils.Constants
import io.rapidz.assignment1.viewmodel.AdminViewModel
import kotlinx.coroutines.delay

@Composable
fun AdminHomeScreen(navController : NavController ?= LocalNavController.current) {

	val context = LocalContext.current
	val viewModel : AdminViewModel = hiltViewModel()
	val candidatesWithScores by viewModel.candidatesWithScores.collectAsState()
	val isGifVisible by viewModel.isGifVisible.collectAsState()
	val searchQuery by viewModel.searchQuery.collectAsState()
	val updatedSearchQuery by rememberUpdatedState(searchQuery)

	val focusManager = LocalFocusManager.current
	val keyboardController = LocalSoftwareKeyboardController.current

	LaunchedEffect(updatedSearchQuery) {
		delay(2000)
		viewModel.searchCandidates()
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

			InputTextFieldTime(
				value = 0,
				onValueChange = {},
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
					value = searchQuery,
					onValueChange = { newQuery -> viewModel.searchQueryChanged(newQuery) },
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
							time = "N/A",
							name = candidateWithScore.candidate.name,
							score = candidateWithScore.totalScore.toString(),
							onClick = {
								// navigate to the AdminTestScreen
								navController?.navigate("${Route.ADMIN_TEST}/${candidateWithScore.candidate.id}")
							}
						)
					}
				}
			}
		}
	}
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
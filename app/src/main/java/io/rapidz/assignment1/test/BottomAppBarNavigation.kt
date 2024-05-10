package io.rapidz.assignment1.test

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.rapidz.assignment1.*
import io.rapidz.assignment1.ui.*
import kotlinx.coroutines.*

@Preview
@Composable
fun BottomAppBarPreview() {
	DefaultTheme {
		BottomAppBar()
	}
}

@Preview
@Composable
fun BottomAppBarNavigationPreview(){
	BottomAppBarGeneral()
}

@Preview
@Composable
fun BottomTestingPreview(){
	DefaultTheme {
		BottomAppBar()
	}
}

@Composable
fun BottomAppBarGeneral() {
	val totalTimeMillis = 60 * 1000L // 1 minute in milliseconds
	val countdownState = remember { mutableLongStateOf(totalTimeMillis) }

	LaunchedEffect(Unit) {
		while (countdownState.longValue > 0) {
			delay(1000) // Wait for 1 second
			countdownState.longValue -= 1000 // Decrement by 1 second
		}
	}

	Scaffold(
		bottomBar = {
			BottomAppBar(
				actions = {
					IconButton(onClick = {}) {
						Icon(Icons.Default.KeyboardDoubleArrowLeft, contentDescription = null)
					}
					IconButton(onClick = {}) {
						Icon(Icons.Default.ChevronLeft, contentDescription = null)
					}
					IconButton(onClick = {}) {
						Icon(Icons.Default.ChevronRight, contentDescription = null)
					}
					IconButton(onClick = {}) {
						Icon(Icons.Default.KeyboardDoubleArrowRight, null)
					}
					Row{
						Spacer(modifier = Modifier.width(spacing_20))
						CountdownText(milliseconds = countdownState.longValue)
					}
				},
				floatingActionButton = {
					FloatingActionButton(
						onClick = {},
						elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
					) {
						Icon(Icons.Default.DoneAll, null)
					}
				}
			)
		},
	) { innerPadding ->
		Text(
			text = "Example of a scaffold with a bottom app bar.",
			Modifier.padding(innerPadding)
		)
	}
}

@Composable
fun BottomAppBar(
	showCountDownTimer : Boolean = true,
	countdownMillis : Long = 0L,
	onLeftDoubleArrowClick : () -> Unit? = {},
	onLeftArrowClick : () -> Unit? = {},
	onRightArrowClick : () -> Unit? = {},
	onRightDoubleArrowClick : () -> Unit? = {},
	onFloatingButtonClick : () -> Unit? = {},
	content: @Composable () -> Unit? = {}
) {
	Scaffold(
		bottomBar = {
			BottomAppBar(
				actions = {
					IconButton(onClick = { onLeftDoubleArrowClick()}) {
						Icon(Icons.Default.KeyboardDoubleArrowLeft, contentDescription = null)
					}
					IconButton(onClick = { onLeftArrowClick()}) {
						Icon(Icons.Default.ChevronLeft, contentDescription = null)
					}
					IconButton(onClick = { onRightArrowClick()}) {
						Icon(Icons.Default.ChevronRight, contentDescription = null)
					}
					IconButton(onClick = { onRightDoubleArrowClick()}) {
						Icon(Icons.Default.KeyboardDoubleArrowRight, null)
					}
					if (showCountDownTimer){
						Row{
							Spacer(modifier = Modifier.width(spacing_20))
							CountdownText(milliseconds = countdownMillis)
						}
					}
				},
				floatingActionButton = {
					FloatingActionButton(
						onClick = { onFloatingButtonClick() },
						elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
					) {
						Icon(Icons.Default.DoneAll, null)
					}
				}
			)
		},
	) {
		innerPadding ->
		Box(
			modifier = Modifier
				.padding(innerPadding)
				.verticalScroll(rememberScrollState())
		)
		content()
	}
}

@Composable
fun CountdownText(milliseconds : Long){
	val minutes = (milliseconds/1000) / 60
	val seconds = (milliseconds / 1000) % 60

	Text(
		text = String.format("%02dm:%02ds", minutes, seconds)
	)
}
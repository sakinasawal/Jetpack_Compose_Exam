package io.rapidz.assignment1.test

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
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
	) { innerPadding ->
		Box(
			modifier = Modifier
				.padding(innerPadding)
				.verticalScroll(rememberScrollState())
		)
		content()
	}
}

@SuppressLint("DefaultLocale")
@Composable
fun CountdownText(milliseconds : Long){
	val minutes = (milliseconds/1000) / 60
	val seconds = (milliseconds / 1000) % 60
	val isCritical = milliseconds <= 60000L

	Text(
		text = String.format("%02dm:%02ds", minutes, seconds),
		color = if (isCritical) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
		style = MaterialTheme.typography.bodyLarge
	)
}

@Preview
@Composable
fun BottomAppBarAdminPreview() {
	AdminTheme {
		BottomAppBarAdmin()
	}
}

@Composable
fun BottomAppBarAdmin(
	showCountDownTimer : Boolean = true,
	countdownMillis : Long = 0L,
	showDoneIcon: Boolean = true,
	showCloseIcon: Boolean = true,
	doneIconColor: Color = Color(0xFF018786),
	closeIcon: ImageVector = Icons.Default.Close,
	closeIconColor: Color = md_theme_admin_error,
	onDoneClick: () -> Unit = {},
	onCloseClick: () -> Unit = {},
	onLeftDoubleArrowClick : () -> Unit? = {},
	onLeftArrowClick : () -> Unit? = {},
	onRightArrowClick : () -> Unit? = {},
	onRightDoubleArrowClick : () -> Unit? = {},
	content: @Composable () -> Unit? = {}
) {
	Scaffold(
		bottomBar = {
			BottomAppBar(
				actions = {
					if (showDoneIcon) {
						IconButton(onClick = { onDoneClick() }) {
							Icon(Icons.Default.Done, contentDescription = null, tint = doneIconColor)
						}
					}
					if (showCloseIcon) {
						IconButton(onClick = { onCloseClick() }) {
							Icon(closeIcon, contentDescription = null, tint = closeIconColor)
						}
					}
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
			)
		},
	) { innerPadding ->
		Box(
			modifier = Modifier
				.padding(innerPadding)
				.verticalScroll(rememberScrollState())
		)
		content()
	}
}

@Composable
fun BottomNavBar(
	countdownMillis: Long,
	currentQuestionIndex : Int,
	totalQuestions : Int,
	onLeftDoubleArrowClick: () -> Unit,
	onLeftArrowClick: () -> Unit,
	onRightArrowClick: () -> Unit,
	onRightDoubleArrowClick: () -> Unit,
	onFloatingButtonClick: () -> Unit,
	content : @Composable () -> Unit
){
	val isCountdownCritical = countdownMillis <= 60 * 1000L

	val backgroundColor = if (isCountdownCritical) {
		MaterialTheme.colorScheme.error
	} else {
		MaterialTheme.colorScheme.primary
	}

	val fabColor = if (isCountdownCritical) {
		MaterialTheme.colorScheme.onError
	} else {
		MaterialTheme.colorScheme.primary
	}

	Scaffold(
		bottomBar = {
			BottomAppBar(
				modifier = Modifier.background(backgroundColor),
				actions = {
					IconButton(onClick = { onLeftDoubleArrowClick() }, enabled = currentQuestionIndex > 0) {
						Icon(Icons.Default.KeyboardDoubleArrowLeft, contentDescription = null)
					}
					IconButton(onClick = { onLeftArrowClick()}, enabled = currentQuestionIndex > 0) {
						Icon(Icons.Default.ChevronLeft, contentDescription = null)
					}
					IconButton(onClick = { onRightArrowClick() }, enabled = currentQuestionIndex < totalQuestions - 1) {
						Icon(Icons.Default.ChevronRight, contentDescription = null)
					}
					IconButton(onClick = { onRightDoubleArrowClick() }, enabled = currentQuestionIndex < totalQuestions - 1) {
						Icon(Icons.Default.KeyboardDoubleArrowRight, null)
					}
					Spacer(modifier = Modifier.width(spacing_20))
					CountdownText(milliseconds = countdownMillis)
				},
				floatingActionButton = {
					FloatingActionButton(
						onClick = { onFloatingButtonClick() },
						containerColor = fabColor
					) {
						Icon(Icons.Default.DoneAll, null)
					}
				}
			)
		}
	) { innerPadding ->
		Box(modifier = Modifier.padding(innerPadding)) {
			content()
		}
	}
}
package io.rapidz.assignment1.test

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import io.rapidz.assignment1.ui.*
import androidx.compose.runtime.*
import androidx.navigation.NavController
import kotlinx.coroutines.delay

@Preview
@Composable
fun TestScreen3(navController: NavController? = null) {
	val totalTimeMillis = 60 * 1000L // 1 minute in milliseconds
	val countdownState = remember { mutableLongStateOf(totalTimeMillis) }

	LaunchedEffect(Unit) {
		while (countdownState.longValue > 0) {
			delay(1000) // Wait for 1 second
			countdownState.longValue -= 1000 // Decrement by 1 second
		}
	}

	DefaultTheme {
		BottomAppBar(
			countdownMillis = countdownState.longValue,
			onLeftDoubleArrowClick = {},
			onLeftArrowClick = {},
			onRightArrowClick = {}
		){
			Question3()
		}
	}
}
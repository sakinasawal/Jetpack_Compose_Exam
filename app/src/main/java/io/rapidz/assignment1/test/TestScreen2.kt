package io.rapidz.assignment1.test

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import io.rapidz.assignment1.R
import io.rapidz.assignment1.*
import io.rapidz.assignment1.ui.*
import kotlinx.coroutines.delay

@Preview
@Composable
fun TestScreen2(navController: NavController? = null){
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
			onLeftArrowClick = {},
			onRightArrowClick = {}
		){
			Question2()
		}
	}
}

@Preview
@Composable
fun TestScreen2Preview(){
	DefaultTheme {
		Question2()
	}
}
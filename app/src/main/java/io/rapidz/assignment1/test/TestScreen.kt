package io.rapidz.assignment1.test

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.*
import androidx.navigation.NavController
import io.rapidz.assignment1.ui.DefaultTheme
import io.rapidz.assignment1.ui.Question1
import io.rapidz.assignment1.ui.RadioButtonAnswer
import kotlinx.coroutines.delay

@Preview
@Composable
fun TestScreen(navController: NavController? = null){
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
			onLeftArrowClick = {
				navController!!.navigate("candidate")
			},
			onRightArrowClick = {
				navController!!.navigate("test2")
			}
		){
			Question1()
		}
	}
}

@Preview
@Composable
fun Question1Preview(){
	Question1()
}

@Preview
@Composable
fun RadioButtonAnswerPreview(){
	RadioButtonAnswer()
}
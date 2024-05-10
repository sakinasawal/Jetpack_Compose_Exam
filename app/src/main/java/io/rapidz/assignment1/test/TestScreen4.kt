package io.rapidz.assignment1.test

import androidx.compose.foundation.background
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.rapidz.assignment1.*
import io.rapidz.assignment1.ui.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import io.rapidz.assignment1.R
import kotlinx.coroutines.delay

@Preview
@Composable
fun TestScreen4(navController: NavController? = null) {
//	val totalTimeMillis = 60 * 1000L // 1 minute in milliseconds
//	val countdownState = remember { mutableLongStateOf(totalTimeMillis) }
//
//	LaunchedEffect(Unit) {
//		while (countdownState.longValue > 0) {
//			delay(1000) // Wait for 1 second
//			countdownState.longValue -= 1000 // Decrement by 1 second
//		}
//	}

	DefaultTheme {
		Question4()
	}
}

@Composable
fun Question4(){
	Column(
		modifier = Modifier
			.fillMaxSize()
			.background(color = md_theme_default_primaryContainer)
			.padding(spacing_10),
		verticalArrangement = Arrangement.Top,
		horizontalAlignment = Alignment.Start
	) {

		TextLabel(
			text = R.string.title_question_4,
			typographyStyle = AppTypography.titleLarge
		)
	}
}
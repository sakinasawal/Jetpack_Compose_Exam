package io.rapidz.assignment1.test

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.rapidz.assignment1.R
import io.rapidz.assignment1.TextLabel
import io.rapidz.assignment1.*
import io.rapidz.assignment1.ui.AppTypography
import io.rapidz.assignment1.ui.md_theme_default_primaryContainer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.navigation.NavController
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import io.rapidz.assignment1.ui.DefaultTheme
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
			onRightArrowClick = {
				Log.d("Navigation", "Navigating to testScreen2")
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
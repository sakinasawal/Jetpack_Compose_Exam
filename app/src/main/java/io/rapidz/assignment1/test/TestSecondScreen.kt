package io.rapidz.assignment1.test

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import io.rapidz.assignment1.spacing_10
import io.rapidz.assignment1.ui.DefaultTheme
import io.rapidz.assignment1.ui.*

enum class QuestionType {
	RADIO_BUTTON,
	CHECKBOX,
	TEXT_INPUT
}


data class Question(
	val textResId: Int,
	val type: QuestionType,
	val options: List<String> = listOf()
)

@Composable
fun TestSecondScreen(questions: List<Question>) {
	var currentQuestionIndex by remember { mutableStateOf(0) }

	DefaultTheme {

	}
}

@Composable
fun BottomAppBarWithNavigation(
	currentQuestionIndex: Int,
	onPreviousClick: () -> Unit,
	onNextClick: () -> Unit
) {
//	BottomAppBar(
//		elevation = 8.dp,
//		backgroundColor = MaterialTheme.colors.primary
//	) {
//		Row(
//			horizontalArrangement = Arrangement.SpaceBetween,
//			modifier = Modifier
//				.fillMaxWidth()
//				.padding(horizontal = 16.dp)
//		) {
//			// Left icon: go to the previous question
//			BottomNavigationButton(
//				icon = { Icon(Icons.Default.ChevronLeft, contentDescription = null) },
//				onClick = onPreviousClick,
//				enabled = currentQuestionIndex > 0
//			)
//
//			// Right icon: go to the next question
//			BottomNavigationButton(
//				icon = { Icon(Icons.Default.ChevronRight, contentDescription = null) },
//				onClick = onNextClick,
//				enabled = currentQuestionIndex < questions.size - 1
//			)
//		}
//	}
}

@Composable
fun BottomAppBarIcon(
	icon : ImageVector,
	selected : Boolean,
	onClick : () -> Unit
){
	IconButton(onClick = onClick) {
		Icon(
			imageVector = icon,
			contentDescription = null,
			tint = if(selected) Color.Blue else Color.Gray
		)
	}
}
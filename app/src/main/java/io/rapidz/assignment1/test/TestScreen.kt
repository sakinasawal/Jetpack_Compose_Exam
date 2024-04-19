package io.rapidz.assignment1.test

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
import io.rapidz.assignment1.ui.DefaultTheme

@Preview
@Composable
fun TestScreen(){
	DefaultTheme {
		Column(
			modifier = Modifier
				.fillMaxSize()
				.background(color = md_theme_default_primaryContainer)
				.padding(all = spacing_10),
			verticalArrangement = Arrangement.Top,
			horizontalAlignment = Alignment.Start
		) {
			TextLabel(
				text = R.string.title_question_1,
				typographyStyle = AppTypography.titleLarge
			)

			Spacer(modifier = Modifier.height(spacing_20))

			TextLabel(
				text = R.string.question_1
			)

			Spacer(modifier = Modifier.height(spacing_10))

			RadioButtonAnswer()
		}
	}
}

@Composable
fun RadioButtonAnswer(){
	val options = listOf("0","18","100","1000++")
	var selectedOption by remember { mutableStateOf(options[0]) }
	Column {
		options.forEach{ option ->
			Row(
				modifier = Modifier
					.padding(all = spacing_4)
					.height(spacing_24)
					.selectable(
						selected = selectedOption == option,
						onClick = { selectedOption = option }
					),
				verticalAlignment = Alignment.CenterVertically
			){
				RadioButton(
					selected = selectedOption == option,
					onClick = null
				)
				Text(
					text = option,
					modifier = Modifier.padding(start = spacing_8)
				)
			}
		}
	}
}

@Composable
fun BottomAppBarQuestion(){

}
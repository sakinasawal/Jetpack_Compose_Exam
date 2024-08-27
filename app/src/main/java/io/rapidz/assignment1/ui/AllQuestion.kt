package io.rapidz.assignment1.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Checkbox
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import io.rapidz.assignment1.*
import io.rapidz.assignment1.viewmodel.CandidateViewModel

@Preview
@Composable
fun Question1(
){
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
						onClick = {
							selectedOption = option
						}
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

@Preview
@Composable
fun Question2() {
	Column(
		modifier = Modifier
			.fillMaxSize()
			.background(color = md_theme_default_primaryContainer)
			.padding(spacing_10),
		verticalArrangement = Arrangement.Top,
		horizontalAlignment = Alignment.Start
	) {

		TextLabel(
			text = R.string.title_question_2,
			typographyStyle = AppTypography.titleLarge
		)

		Spacer(modifier = Modifier.height(spacing_20))

		TextLabel(text = R.string.question_2)

		Spacer(modifier = Modifier.height(spacing_10))

		CheckBoxAnswer()
	}
}

@Composable
fun CheckBoxAnswer(){
	val activities = listOf("Sleep", "Lay down", "Movie", "Eat")
	val checkedList = remember { mutableStateListOf<Boolean>() }

	Column(
		modifier = Modifier
			.fillMaxSize()
	) {
		activities.forEachIndexed{ index, activity ->
			Row(verticalAlignment = Alignment.CenterVertically) {
				Checkbox(
					checked = checkedList.getOrNull(index) ?: false,
					onCheckedChange = { isChecked ->
						checkedList[index] = isChecked
					}
				)
				Text(text = activity)
			}
		}
	}
}

@Preview
@Composable
fun Question3(){
	Column(
		modifier = Modifier
			.fillMaxSize()
			.background(color = md_theme_default_primaryContainer)
			.padding(spacing_10),
		verticalArrangement = Arrangement.Top,
		horizontalAlignment = Alignment.Start
	) {

		TextLabel(
			text = R.string.title_question_3,
			typographyStyle = AppTypography.titleLarge
		)

		Spacer(modifier = Modifier.height(spacing_20))

		TextLabel(text = R.string.question_2)

		Spacer(modifier = Modifier.height(spacing_4))

		Textarea()
	}
}

@Composable
fun Textarea() {
	val text = remember { mutableStateOf("") }
	TextField(
		value = text.value,
		onValueChange = { text.value = it },
		modifier = Modifier
			.fillMaxWidth()
			.fillMaxHeight(0.9f)
			.padding(spacing_4)
			.border(width = spacing_1, color = Color.Black)
	)
}
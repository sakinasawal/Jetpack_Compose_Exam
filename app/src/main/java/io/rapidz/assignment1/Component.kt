package io.rapidz.assignment1

import android.annotation.SuppressLint
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.graphics.Color
import io.rapidz.assignment1.ui.AppTypography
import io.rapidz.assignment1.ui.DefaultTheme
import io.rapidz.assignment1.ui.md_theme_default_primaryContainer

@Composable
@SuppressLint("ModifierParameter")
fun TextLabel(
	text : Int,
	modifier : Modifier = Modifier,
	typographyStyle: TextStyle = LocalTextStyle.current
){
	Text(
		text = stringResource(id = text),
		style = typographyStyle,
		modifier = modifier)
}

@Composable
fun InputTextField(
	value : String,
	onValueChange : (String)->Unit,
	placeholder: String,
	modifier : Modifier = Modifier
){
	TextField(
		value = value,
		onValueChange = onValueChange,
		modifier = modifier.fillMaxWidth(),
		placeholder = { Text(placeholder) }
	)
}

@Composable
fun AppButton(
	@StringRes textRes: Int,
	onClick: () -> Unit,
	modifier: Modifier = Modifier,
	enabled: Boolean = true,
	shape: Shape = ButtonDefaults.shape,
	colors: ButtonColors = ButtonDefaults.buttonColors(),
	textColor: Color? = Color.White
) = Button(
	onClick = onClick,
	modifier = modifier,
	enabled = enabled,
	shape = shape,
	colors = colors,
) {
	if (textColor != null) {
		Text(textRes)
	}
}

@Composable
fun Text(@StringRes res: Int) = Text(stringResource(res))

@Composable
fun GeneralAlertDialog(
	@StringRes titleResId : Int,
	@StringRes messageResId : Int,
	@StringRes msgResId : Int? = null,
	onPositiveButtonClick : () -> Unit,
	onNegativeButtonClick : () -> Unit
){
	AlertDialog(
		onDismissRequest = {},
		icon = {
			Icon(Icons.Default.Bolt, contentDescription = null)
		},
		title = { Text(titleResId)},
		text = {
			Column(
				modifier = Modifier
					.padding(spacing_8)
			) {
				Text(messageResId)

				Spacer(modifier = Modifier.height(spacing_10))
				
				if (msgResId != null) {
					Text(msgResId)
				}
			}

		},
		confirmButton = {
			TextButton(onClick = { onPositiveButtonClick() }) {
				Text(R.string.dialog_yes)
			}
		},
		dismissButton = {
			TextButton(onClick = { onNegativeButtonClick() }) {
				Text(R.string.dialog_no)
			}
		}
	)
}

@Composable
fun Question1(){
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


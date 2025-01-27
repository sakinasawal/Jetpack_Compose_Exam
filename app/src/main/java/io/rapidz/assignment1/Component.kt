package io.rapidz.assignment1

import android.annotation.SuppressLint
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.navigation.NavController
import kotlinx.coroutines.delay

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
@SuppressLint("ModifierParameter")
fun TextLabelTitle(
	text : String,
	modifier : Modifier = Modifier,
	typographyStyle: TextStyle = LocalTextStyle.current
){
	Text(
		text = text,
		style = typographyStyle,
		modifier = modifier)
}

@Composable
fun InputTextField(
	value : String,
	onValueChange : (String)->Unit,
	placeholder: String,
	modifier : Modifier = Modifier,
){
	TextField(
		value = value,
		onValueChange = onValueChange,
		modifier = modifier.fillMaxWidth(),
		placeholder = { Text(placeholder) }
	)
}

@SuppressLint("ModifierParameter")
@Composable
fun InputTextFieldAdmin(
    value : Int,
    onValueChange : (Int)->Unit,
    label: String,
    placeholder: String,
	modifier : Modifier = Modifier,
){
	var textValue by remember(value) { mutableStateOf(if (value == 0) "" else value.toString()) }
	println("DEBUG: TextField Value = $value")
	TextField(
		value = textValue,
		onValueChange = { newValue ->
			if (newValue.all { it.isDigit() }) {
				textValue = newValue
				onValueChange(newValue.toIntOrNull() ?: 0)
			}
		},
		modifier = modifier.fillMaxWidth(),
		label = { Text(label) },
		placeholder = {Text(placeholder)},
		keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
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
) {
	Button(
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
}

@Composable
fun Text(@StringRes res: Int) = Text(stringResource(res))

@Composable
fun GeneralAlertDialog(
	@StringRes titleResId : Int,
	@StringRes messageResId : Int,
	msg : String? = null,
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
				
				if (msg != null) {
					Text(msg)
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
fun EndTestAlertDialog(
	@StringRes titleResId : Int,
	@StringRes messageResId : Int,
	onPositiveButtonClick : () -> Unit,
	onNegativeButtonClick : () -> Unit
){
	AlertDialog(
		onDismissRequest = {},
		icon = {
			Icon(Icons.Default.Done, contentDescription = null)
		},
		title = { Text(titleResId)},
		text = {
			Column(
				modifier = Modifier
					.padding(spacing_8)
			) {
				Text(messageResId)
			}

		},
		confirmButton = {
			TextButton(onClick = { onPositiveButtonClick() }) {
				Text(R.string.dialog_ok)
			}
		},
		dismissButton = {
			TextButton(onClick = { onNegativeButtonClick() }) {
				Text(R.string.dialog_no)
			}
		}
	)
}

@SuppressLint("DefaultLocale")
fun formatSecondsToTime(seconds: Int): String {
	val minutes = seconds / 60
	val remainingSeconds = seconds % 60
	return String.format("%02dm %02ds", minutes, remainingSeconds)
}
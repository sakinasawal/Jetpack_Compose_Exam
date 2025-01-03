package io.rapidz.assignment1

import android.annotation.SuppressLint
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.graphics.Color
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
fun EndTestAlertDialog(
	@StringRes titleResId : Int,
	@StringRes messageResId : Int,
	onPositiveButtonClick : () -> Unit
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
		}
	)
}

@Composable
fun CountdownLogic(countdownState: MutableState<Long>) {
	LaunchedEffect(Unit) {
		while (countdownState.value > 0) {
			delay(1000) // Wait for 1 second
			countdownState.value -= 1000 // Decrement by 1 second
		}
	}
}

@Composable
fun CountdownScreen(navController: NavController? = null) {
	val totalTimeMillis = 60 * 1000L // 1 minute in milliseconds
	val countdownState = remember { mutableStateOf(totalTimeMillis) }

	CountdownLogic(countdownState)
}
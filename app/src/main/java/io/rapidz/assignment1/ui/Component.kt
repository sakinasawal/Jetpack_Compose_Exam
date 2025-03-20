package io.rapidz.assignment1.ui

import android.annotation.SuppressLint
import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.KeyboardDoubleArrowLeft
import androidx.compose.material.icons.filled.KeyboardDoubleArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.request.ImageRequest
import io.rapidz.assignment1.R
import io.rapidz.assignment1.data.Role
import io.rapidz.assignment1.utils.Constants
import java.util.regex.Pattern

// region Label & InputText ===============================================

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

@SuppressLint("ModifierParameter")
@Composable
fun InputTextField(
	value : String,
	onValueChange : (String)->Unit,
	placeholder: String,
	modifier : Modifier = Modifier,
){
	val keyboardController = LocalSoftwareKeyboardController.current

	TextField(
		value = value,
		onValueChange = onValueChange,
		modifier = modifier.fillMaxWidth(),
		placeholder = { Text(placeholder) },
		keyboardOptions = KeyboardOptions.Default.copy(
			imeAction = ImeAction.Done
		),
		keyboardActions = KeyboardActions(
			onDone = {
				keyboardController?.hide()
			}
		),
	)
}

@SuppressLint("ModifierParameter")
@Composable
fun InputTextSearch(
	value : String,
	onValueChange : (String)->Unit,
	label : String? = null,
	placeholder: String,
	modifier : Modifier = Modifier,
){
	val keyboardController = LocalSoftwareKeyboardController.current

	TextField(
		value = value,
		onValueChange = onValueChange,
		modifier = modifier.fillMaxWidth(),
		label = { label?.let {Text(it) } },
		placeholder = { Text(placeholder) },
		keyboardOptions = KeyboardOptions.Default.copy(
			imeAction = ImeAction.Done
		),
		keyboardActions = KeyboardActions(
			onDone = {
				keyboardController?.hide()
			}
		),
	)
}

@SuppressLint("ModifierParameter")
@Composable
fun InputTextFieldTime(
    value : Long,
    onValueChange : (Long)->Unit,
    label: String,
    placeholder: String,
	modifier : Modifier = Modifier,
){
	var textValue by remember(value) { mutableStateOf(value.toString()) }

	TextField(
		value = textValue,
		onValueChange = { newValue ->
			if (newValue.all { it.isDigit() } || newValue.isEmpty()) {
				textValue = newValue
				onValueChange(newValue.toLongOrNull() ?: 0)
			}
		},
		modifier = modifier.fillMaxWidth(),
		label = { Text(label) },
		placeholder = { Text(placeholder) },
		keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
	)
}

// end region

// region Button =============================================

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
			Text(stringResource(textRes))
		}
	}
}

// end region

// region Dialog ==================================================

@Composable
fun GeneralAlertDialog(
	@StringRes titleResId : Int,
	@StringRes msgResId : Int? = null,
	msg : String? = null,
	singleButton: Boolean = false,
	@StringRes positiveBtnLbl: Int = R.string.dialog_ok,
	@StringRes negativeBtnLbl: Int = R.string.dialog_no,
	onDismissRequest: () -> Unit,
	onPositiveButtonClick : (() -> Unit)? = null,
	onNegativeButtonClick : (() -> Unit)? = null
){
	AlertDialog(
		onDismissRequest = onDismissRequest,
		icon = { Icon(Icons.Default.Bolt, contentDescription = null) },
		title = { Text(stringResource(titleResId))},
		text = {
			Column(modifier = Modifier.padding(spacing_8)) {
				msgResId?.let { Text(stringResource(it)) }
				Spacer(modifier = Modifier.height(spacing_10))
				msg?.let { Text(it) }
			}
		},
		confirmButton = {
			TextButton(onClick = { onPositiveButtonClick?.invoke()
				onDismissRequest()
			}){
				Text(stringResource(positiveBtnLbl))
			}
		},
		dismissButton = {
			if(!singleButton){
				TextButton(onClick = { onNegativeButtonClick?.invoke()
					onDismissRequest()
				}){
					Text(stringResource(negativeBtnLbl))
				}
			}
		}
	)
}

// end region

// region Bottom Nav Bar ==============================================

@Preview
@Composable
fun BottomAppBarPreview() {
	DefaultTheme {
		BottomAppBar(role = Role(Constants.Role.ROLE_CANDIDATE),
			timerText = "00m:00s",
			showFloatBtn = true)
	}
}

@Preview
@Composable
fun BottomAppBarAdminPreview() {
	AdminTheme {
		BottomAppBar(role = Role(Constants.Role.ROLE_ADMIN),
			timerText = "00m:00s",
			showDoneIcon = true,
			showCloseIcon = true)
	}
}

@Composable
fun BottomAppBar(
	role : Role,
	timerText: String,
	showDoneIcon: Boolean? = false,
	showCloseIcon: Boolean? = false,
	doneIconColor: Color = Color(0xFF018786),
	closeIcon: ImageVector = Icons.Default.Close,
	closeIconColor: Color = md_theme_admin_error,
	onDoneClick: (() -> Unit)? = null,
	onCloseClick: (() -> Unit)? = null,
	onLeftDoubleArrowClick : () -> Unit? = {},
	onLeftArrowClick : () -> Unit? = {},
	onRightArrowClick : () -> Unit? = {},
	onRightDoubleArrowClick : () -> Unit? = {},
	showFloatBtn : Boolean? = false,
	onFloatingButtonClick : (() -> Unit)? = null,
	content: @Composable () -> Unit = {}
) {

	val remainingSeconds = remember(timerText) {
		val parts = timerText.split("m", "s").map { it.trim() }
		val minutes = parts.getOrNull(0)?.toIntOrNull() ?: 0
		val seconds = parts.getOrNull(1)?.toIntOrNull() ?: 0
		(minutes * 60) + seconds
	}

	val bottomAppBarColor = if (remainingSeconds < 60) md_theme_default_error else MaterialTheme.colorScheme.surface
	val fabContainerColor = if (remainingSeconds < 60) md_theme_default_onError else FloatingActionButtonDefaults.containerColor

	Scaffold(
		bottomBar = {
			BottomAppBar(
				containerColor = bottomAppBarColor,
				actions = {
					if (role.isAdmin()){
						if (showDoneIcon == true) {
							IconButton(onClick = { onDoneClick?.invoke()}) {
								Icon(Icons.Default.Done, contentDescription = null, tint = doneIconColor)
							}
						}
						if (showCloseIcon == true) {
							IconButton(onClick = { onCloseClick?.invoke() }) {
								Icon(closeIcon, contentDescription = null, tint = closeIconColor)
							}
						}
					}
					IconButton(onClick = { onLeftDoubleArrowClick() }) {
						Icon(Icons.Default.KeyboardDoubleArrowLeft, contentDescription = null)
					}
					IconButton(onClick = { onLeftArrowClick() }) {
						Icon(Icons.Default.ChevronLeft, contentDescription = null)
					}
					IconButton(onClick = { onRightArrowClick() }) {
						Icon(Icons.Default.ChevronRight, contentDescription = null)
					}
					IconButton(onClick = { onRightDoubleArrowClick() }) {
						Icon(Icons.Default.KeyboardDoubleArrowRight, null)
					}
					Row{
						Spacer(modifier = Modifier.width(spacing_20))
						Text(
							text = timerText,
							style = if (role.isCandidate()){
								MaterialTheme.typography.bodyMedium
							} else {
								MaterialTheme.typography.bodyLarge
							}
						)
					}
				},
				floatingActionButton = {
					if (role.isCandidate() && showFloatBtn == true) {
						FloatingActionButton(
							onClick = { onFloatingButtonClick?.invoke() },
							containerColor = fabContainerColor,
							elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
						) {
							Icon(Icons.Default.DoneAll, null)
						}
					}
				}
			)
		},
	) { innerPadding ->
		Box(
			modifier = Modifier
				.padding(innerPadding)
				.verticalScroll(rememberScrollState())
		)
		content()
	}
}

// end region

// region format timer ================================================

@SuppressLint("DefaultLocale")
fun formatSecondsToTime(seconds: Long): String {
	val minutes = seconds / 60
	val remainingSeconds = seconds % 60
	return String.format("%02dm %02ds", minutes, remainingSeconds)
}

// end region

// region Regex email ================================================

fun isValidEmail(email : String) : Boolean {
	val emailRegex = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})"
	val pattern = Pattern.compile(emailRegex)
	return pattern.matcher(email).matches()
}

// end region

// region Table ======================================================

@Composable
fun GifImage(context : Context, url : String) {
	AsyncImage(
		model = ImageRequest.Builder(context)
			.data(url)
			.crossfade(true)
			.decoderFactory(GifDecoder.Factory())
			.build(),
		contentDescription = null,
		modifier = Modifier
			.fillMaxSize(),
		contentScale = ContentScale.Crop
	)
}

@SuppressLint("ModifierParameter")
@Composable
fun TableHeader(
	headers : List<String>,
	weights : List<Float>,
	backgroundColor : Color = md_theme_admin_primaryContainer,
	textColor : Color = Color.Black,
	modifier : Modifier = Modifier
){
	Row(
		modifier = modifier
			.fillMaxWidth()
			.background(color = backgroundColor)
			.padding(vertical = spacing_8, horizontal = spacing_16),
		horizontalArrangement = Arrangement.SpaceBetween
	) {
		headers.forEachIndexed { index, header ->
			Text(
				text = header,
				modifier = Modifier.weight(weights.getOrElse(index) { 1f }),
				color = textColor
			)
		}
	}
}

@Composable
fun TableRow(
	time: String,
	name: String,
	score: String,
	onClick: () -> Unit
){
	Row(
		modifier = Modifier
			.fillMaxWidth()
			.background(color = md_theme_admin_tertiaryContainer)
			.clickable(onClick = onClick)
			.padding(vertical = spacing_8, horizontal = spacing_16),
		horizontalArrangement = Arrangement.SpaceBetween
	) {
		Text(text = time, modifier = Modifier.weight(1f))
		Text(text = name, modifier = Modifier.weight(2f))
		Text(text = score, modifier = Modifier.weight(1f))
	}
}

// end region

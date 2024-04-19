package io.rapidz.assignment1.candidate

import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import io.rapidz.assignment1.R
import io.rapidz.assignment1.TextLabel
import io.rapidz.assignment1.ui.AppTypography
import androidx.compose.ui.Modifier
import io.rapidz.assignment1.*
import io.rapidz.assignment1.ui.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import java.util.regex.Pattern

@Preview
@Composable
fun CandidateScreen(navController: NavController? = null) {

	var name by remember { mutableStateOf("") }
	var emailAddress by remember { mutableStateOf("") }

	val isRegisterEnable by remember(name, emailAddress) {
		derivedStateOf {
			name.isNotBlank() && isValidEmail(emailAddress)
		}
	}

	val focusManager = LocalFocusManager.current
	val keyboardController = LocalSoftwareKeyboardController.current

	DefaultTheme {
		Column(
			modifier = Modifier
				.fillMaxSize()
				.background(color = md_theme_default_background)
				.padding(all = spacing_20)
				.pointerInput(Unit) {
					detectTapGestures(onTap = {
						keyboardController?.hide()
						focusManager.clearFocus()
					})
				},
			verticalArrangement = Arrangement.spacedBy(spacing_20),
			horizontalAlignment = Alignment.Start
		) {

			TextLabel(
				text = R.string.candidate_registration,
				typographyStyle = AppTypography.titleLarge
			)

			InputTextField(
				value = name,
				onValueChange = { name = it },
				placeholder = stringResource(id = R.string.name)
			)

			InputTextField(
				value = emailAddress,
				onValueChange = { emailAddress = it },
				placeholder = stringResource(id = R.string.email_address)
			)

			Spacer(Modifier.weight(1f))

			AppButton(
				textRes = R.string.register,
				onClick = {
					if (isRegisterEnable){
						navController!!.navigate("test")
					}
				},
				modifier = Modifier.fillMaxWidth(),
				enabled = isRegisterEnable
			)
		}
	}

}

private fun isValidEmail(email : String) : Boolean {
	val emailRegex = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})"
	val pattern = Pattern.compile(emailRegex)
	return pattern.matcher(email).matches()
}



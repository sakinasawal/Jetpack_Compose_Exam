package io.rapidz.assignment1.ui.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import io.rapidz.assignment1.AppButton
import io.rapidz.assignment1.InputTextField
import io.rapidz.assignment1.LocalNavController
import io.rapidz.assignment1.R
import io.rapidz.assignment1.Screen
import io.rapidz.assignment1.TextLabel
import io.rapidz.assignment1.ui.spacing_20
import io.rapidz.assignment1.ui.AdminTheme
import io.rapidz.assignment1.ui.AppTypography
import io.rapidz.assignment1.ui.md_theme_default_background

@Composable
fun AdminScreen(navController: NavController? = LocalNavController.current) {

	var password by remember { mutableStateOf("") }
	var showSnackbar by remember { mutableStateOf(false) }

	val isLoginEnabled by remember(password) {
		derivedStateOf { password.isNotEmpty() }
	}

	val focusManager = LocalFocusManager.current
	val keyboardController = LocalSoftwareKeyboardController.current

	val snackbarHostState = remember { SnackbarHostState() }

	val wrongPasswordMessage = stringResource(id = R.string.wrong_password)
	val correctPassword = stringResource(id = R.string.password_value)

	LaunchedEffect(showSnackbar) {
		if (showSnackbar) {
			snackbarHostState.showSnackbar(wrongPasswordMessage)
			showSnackbar = false
		}
	}

	AdminTheme {
		Box(
			modifier = Modifier
				.fillMaxSize()
				.background(color = md_theme_default_background)
		){
			AdminForm(
				password = password,
				isLoginEnable = isLoginEnabled,
				onNameChange = { newPassword ->
					password = newPassword
				},
				onLoginClick = {
					if (password == correctPassword) {
						navController?.navigate(Screen.AdminHome.route)
					} else {
						showSnackbar = true
						password = ""
					}
				},
				onBackgroundTap = {
					keyboardController?.hide()
					focusManager.clearFocus()
				}
			)

			SnackbarHost(
				hostState = snackbarHostState,
				modifier = Modifier.align(Alignment.BottomCenter)
			)
		}
	}
}

@Composable
private fun AdminForm(
	password : String,
	isLoginEnable : Boolean,
	onNameChange : (String) -> Unit,
	onLoginClick: () -> Unit,
	onBackgroundTap : () -> Unit
){
	Column(
		modifier = Modifier
			.fillMaxSize()
			.background(color = md_theme_default_background)
			.padding(all = spacing_20)
			.pointerInput(Unit) {
				detectTapGestures(onTap = { onBackgroundTap() })
			},
		verticalArrangement = Arrangement.spacedBy(spacing_20),
		horizontalAlignment = Alignment.Start
	) {

		TextLabel(
			text = R.string.admin,
			typographyStyle = AppTypography.titleLarge
		)

		InputTextField(
			value = password,
			onValueChange = onNameChange,
			placeholder = stringResource(id = R.string.password)
		)

		Spacer(Modifier.weight(1f))

		AppButton(
			textRes = R.string.login,
			onClick = onLoginClick,
			modifier = Modifier.fillMaxWidth(),
			enabled = isLoginEnable
		)
	}
}

@Preview
@Composable
private fun AdminScreenPreview(){
	AdminScreen()
}
package io.rapidz.assignment1.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import io.rapidz.assignment1.AppButton
import io.rapidz.assignment1.InputTextField
import io.rapidz.assignment1.R
import io.rapidz.assignment1.TextLabel
import io.rapidz.assignment1.spacing_10
import io.rapidz.assignment1.spacing_20
import io.rapidz.assignment1.ui.AdminTheme
import io.rapidz.assignment1.ui.AppTypography
import io.rapidz.assignment1.ui.md_theme_admin_onPrimaryContainer
import io.rapidz.assignment1.ui.md_theme_default_background
import io.rapidz.assignment1.ui.md_theme_default_primaryContainer

@Composable
fun AdminScreen() {
	AdminTheme {}
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
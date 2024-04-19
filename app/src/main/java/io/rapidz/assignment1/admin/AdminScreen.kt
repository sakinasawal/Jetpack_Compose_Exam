package io.rapidz.assignment1.admin

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import io.rapidz.assignment1.R
import io.rapidz.assignment1.TextLabel
import io.rapidz.assignment1.ui.AppTypography

@Composable
fun AdminScreen() {
	TextLabel(
		text = R.string.admin,
		typographyStyle = AppTypography.titleLarge
	)
}

@Preview
@Composable
private fun AdminScreenPreview(){
	AdminScreen()
}
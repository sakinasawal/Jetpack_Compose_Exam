package io.rapidz.assignment1.test

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import io.rapidz.assignment1.R
import io.rapidz.assignment1.TextLabel
import io.rapidz.assignment1.ui.AppTypography

@Preview
@Composable
fun LastTestDialog() {

	TextLabel(
		text = R.string.test_screen,
		typographyStyle = AppTypography.titleLarge
	)
}
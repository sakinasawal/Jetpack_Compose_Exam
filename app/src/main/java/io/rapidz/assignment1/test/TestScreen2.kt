package io.rapidz.assignment1.test

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.rapidz.assignment1.R
import io.rapidz.assignment1.TextLabel
import io.rapidz.assignment1.spacing_10
import io.rapidz.assignment1.ui.AppTypography
import io.rapidz.assignment1.ui.md_theme_default_primaryContainer


@Composable
fun TestScreen2() {
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
	}
}
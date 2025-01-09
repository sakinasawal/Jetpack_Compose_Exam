package io.rapidz.assignment1.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.rapidz.assignment1.R
import io.rapidz.assignment1.TextLabel
import io.rapidz.assignment1.spacing_20
import io.rapidz.assignment1.ui.AppTypography
import io.rapidz.assignment1.ui.md_theme_default_background
import androidx.compose.material3.*

@Composable
fun AdminTestScreen(
	candidateId : Long,
) {
	Column(
		modifier = Modifier
			.fillMaxSize()
			.background(color = md_theme_default_background),
		verticalArrangement = Arrangement.spacedBy(spacing_20),
		horizontalAlignment = Alignment.Start
	) {
		TextLabel(
			text = R.string.admin_taken_tests,
			typographyStyle = AppTypography.titleLarge
		)

		Text(text = "Admin Test Screen for Candidate ID: $candidateId")
	}
}
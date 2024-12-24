package io.rapidz.assignment1.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Checkbox
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import io.rapidz.assignment1.*
import io.rapidz.assignment1.viewmodel.CandidateViewModel
import io.rapidz.assignment1.viewmodel.TestViewModel

@Composable
fun Question1(
){
	Column(
		modifier = Modifier
			.fillMaxSize()
			.background(color = md_theme_default_primaryContainer)
			.padding(all = spacing_10),
		verticalArrangement = Arrangement.Top,
		horizontalAlignment = Alignment.Start
	) {
		TextLabel(
			text = R.string.title_question_1,
			typographyStyle = AppTypography.titleLarge
		)

		Spacer(modifier = Modifier.height(spacing_20))

		TextLabel(
			text = R.string.question_1
		)

		Spacer(modifier = Modifier.height(spacing_10))

//		RadioButtonAnswer()
	}
}



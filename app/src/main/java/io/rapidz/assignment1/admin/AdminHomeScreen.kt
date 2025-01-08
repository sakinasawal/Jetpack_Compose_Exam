package io.rapidz.assignment1.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import io.rapidz.assignment1.R
import io.rapidz.assignment1.TextLabel
import io.rapidz.assignment1.spacing_10
import io.rapidz.assignment1.ui.AppTypography

@Composable
fun AdminHomeScreen() {
	Column(
		modifier = Modifier
			.fillMaxSize()
			.background(color = Color.White)
			.padding(spacing_10),
		verticalArrangement = Arrangement.Top,
		horizontalAlignment = Alignment.Start
	){
		TextLabel(
			text = R.string.admin_home,
			typographyStyle = AppTypography.titleLarge
		)
	}
}



@Preview
@Composable
private fun AdminScreenPreview(){
	AdminHomeScreen()
}
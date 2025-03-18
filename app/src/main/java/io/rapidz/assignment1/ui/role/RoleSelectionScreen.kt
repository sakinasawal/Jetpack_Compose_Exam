package io.rapidz.assignment1.ui.role

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import io.rapidz.assignment1.navigate
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import io.rapidz.assignment1.ui.AppButton
import io.rapidz.assignment1.R
import io.rapidz.assignment1.ui.AppTypography
import io.rapidz.assignment1.ui.TextLabel
import io.rapidz.assignment1.*
import io.rapidz.assignment1.ui.AdminTheme
import io.rapidz.assignment1.ui.DefaultTheme
import io.rapidz.assignment1.ui.md_theme_default_primaryContainer
import io.rapidz.assignment1.ui.spacing_10


@Composable
fun RoleSelectionScreen(navController: NavController? = LocalNavController.current) {

	Column(
		modifier = Modifier
			.fillMaxSize()
			.background(color = md_theme_default_primaryContainer)
			.padding(spacing_10),
		verticalArrangement = Arrangement.Top,
		horizontalAlignment = Alignment.Start
	){

		TextLabel(
			text = R.string.select_your_role,
			typographyStyle = AppTypography.titleLarge
		)

		Column(
			modifier = Modifier
				.fillMaxSize(),
			verticalArrangement = Arrangement.Center,
			horizontalAlignment = Alignment.CenterHorizontally
		) {

			AdminTheme {
				AppButton(
					textRes = R.string.admin,
					onClick = {navController?.navigate(Screen.Admin)},
					modifier = Modifier.fillMaxWidth()
				)
			}

			Spacer(modifier = Modifier.height(spacing_10))

			DefaultTheme {
				AppButton(
					textRes = R.string.candidate,
					onClick = {
						 navController?.navigate(Screen.CandidateRegister)
					},
					modifier = Modifier.fillMaxWidth()
				)
			}
		}
	}
}

@Preview
@Composable
fun RoleSelectionScreenPreview(){
	RoleSelectionScreen()
}

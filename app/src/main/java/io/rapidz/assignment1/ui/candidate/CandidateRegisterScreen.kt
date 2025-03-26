package io.rapidz.assignment1.ui.candidate

import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import io.rapidz.assignment1.R
import io.rapidz.assignment1.ui.TextLabel
import io.rapidz.assignment1.ui.AppTypography
import androidx.compose.ui.Modifier
import io.rapidz.assignment1.*
import io.rapidz.assignment1.ui.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.hilt.navigation.compose.hiltViewModel
import io.rapidz.assignment1.data.CandidateUiState
import io.rapidz.assignment1.viewmodel.CandidateViewModel

@Composable
fun CandidateRegister(navController: NavController? = LocalNavController.current,
					  viewModel: CandidateViewModel = hiltViewModel()
) {
	val context = LocalContext.current
	val candidateUiState by viewModel.uiState.collectAsState()
	val toastMessage by viewModel.toastMessage.collectAsState()
	val isRegisterEnable = candidateUiState.name.isNotBlank() && candidateUiState.email.isNotBlank() && isValidEmail(candidateUiState.email)

	val focusManager = LocalFocusManager.current
	val keyboardController = LocalSoftwareKeyboardController.current

	viewModel.let {
		CandidateScreenRegisterForm(
			name = candidateUiState.name,
			emailAddress = candidateUiState.email,
			isRegisterEnable = isRegisterEnable,
			onNameChange = it::onNameChange,
			onEmailAddressChange = it::onEmailChange,
			onRegisterClick = {
				it.registerCandidate{ candidateId, usePreviousData ->
					navController?.navigate("${Route.TEST}/$candidateId?${Key.USE_PREVIOUS_DATA}=${usePreviousData}")
				}
			},
			onBackgroundTap = {
				keyboardController?.hide()
				focusManager.clearFocus()
			}
		)
	}


	toastMessage?.let { message ->
		Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
		viewModel.clearToastMessage()
	}

	if (candidateUiState.showDialog){
		ShowAlertDialog(
			candidateName = candidateUiState.name,
			onContinue = {
				viewModel.onContinueTest()
				navController?.navigate("${Route.TEST}/${candidateUiState.candidateId}?${Key.USE_PREVIOUS_DATA}=true")
			},
			onNewTest = {
				viewModel.onStartNewTest()
				navController?.navigate("${Route.TEST}/${candidateUiState.candidateId}?${Key.USE_PREVIOUS_DATA}=false")
			}
		)
	}
}

@Composable
private fun CandidateScreenRegisterForm(
	name : String,
	emailAddress : String,
	isRegisterEnable : Boolean,
	onNameChange : (String) -> Unit,
	onEmailAddressChange : (String) -> Unit,
	onRegisterClick: () -> Unit,
	onBackgroundTap : () -> Unit
){
	DefaultTheme {
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
				text = R.string.candidate_registration,
				typographyStyle = AppTypography.titleLarge
			)

			InputTextField(
				value = name,
				onValueChange = onNameChange,
				placeholder = stringResource(id = R.string.name)
			)

			InputTextField(
				value = emailAddress,
				onValueChange = onEmailAddressChange,
				placeholder = stringResource(id = R.string.email_address)
			)

			Spacer(Modifier.weight(1f))

			AppButton(
				textRes = R.string.register,
				onClick = onRegisterClick,
				modifier = Modifier.fillMaxWidth(),
				enabled = isRegisterEnable
			)
		}
	}
}

@Composable
fun ShowAlertDialog(
	candidateName : String = "",
	onContinue: (() -> Unit) ? = null,
	onNewTest: (() -> Unit) ? = null
){
	DefaultTheme {
		GeneralAlertDialog(
			titleResId = R.string.title_last_test,
			msgResId = R.string.message_last_test,
			msg = stringResource(R.string.candidate_dialog, candidateName),
			positiveBtnLbl = R.string.dialog_yes,
			negativeBtnLbl = R.string.dialog_no,
			onDismissRequest = { },
			onPositiveButtonClick = { onContinue?.let { it() } },
			onNegativeButtonClick = { onNewTest?.let { it() } }
		)
	}
}

@Preview
@Composable
fun CandidateRegisterPreview(){
	CandidateScreenRegisterForm(
		name = "Sakina",
		emailAddress = "sakina@gmail.com",
		isRegisterEnable = true,
		onNameChange = {},
		onEmailAddressChange = {},
		onRegisterClick = {},
		onBackgroundTap = {}
	)
}

@Preview
@Composable
fun ShowAlertDialogPreview(){
	ShowAlertDialog(
		candidateName = "Sakina"
	)
}
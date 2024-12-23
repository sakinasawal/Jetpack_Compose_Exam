package io.rapidz.assignment1.candidate

import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.tooling.preview.Preview
import io.rapidz.assignment1.R
import io.rapidz.assignment1.TextLabel
import io.rapidz.assignment1.ui.AppTypography
import androidx.compose.ui.Modifier
import io.rapidz.assignment1.*
import io.rapidz.assignment1.ui.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import io.rapidz.assignment1.data.Candidate
import io.rapidz.assignment1.viewmodel.CandidateViewModel
import java.util.regex.Pattern
import androidx.compose.ui.platform.LocalContext
import io.rapidz.assignment1.repository.CandidateRepository
import io.rapidz.assignment1.storage.AppDatabase
import io.rapidz.assignment1.viewmodel.CandidateDataStoreViewModel
import io.rapidz.assignment1.viewmodel.CandidateDataStoreViewModelFactory
import io.rapidz.assignment1.viewmodel.CandidateViewModelFactory
import kotlinx.coroutines.launch

@Preview
@Composable
fun CandidateScreen(navController: NavController? = null) {

	val context = LocalContext.current

	val candidateDataStoreViewModel: CandidateDataStoreViewModel = viewModel(
		factory = CandidateDataStoreViewModelFactory(context)
	)

	val database = remember { AppDatabase.getDatabase(context) }
	val candidateRepository = remember { CandidateRepository(database.candidateDao()) }
	val viewModel: CandidateViewModel = viewModel(factory = CandidateViewModelFactory(candidateRepository))

	val name by candidateDataStoreViewModel.name.collectAsState()
	val emailAddress by candidateDataStoreViewModel.email.collectAsState()

	val isRegisterEnable by remember(name, emailAddress) {
		derivedStateOf {
			name.isNotBlank() && emailAddress.isNotBlank() && isValidEmail(emailAddress)
		}
	}

	val focusManager = LocalFocusManager.current
	val keyboardController = LocalSoftwareKeyboardController.current

	val scope = rememberCoroutineScope()

	DefaultTheme {
		CandidateScreenRegisterForm(
			name = name,
			emailAddress = emailAddress,
			isRegisterEnable = isRegisterEnable,
			onNameChange = { newName ->
				scope.launch {
					candidateDataStoreViewModel.saveCandidateData(context, newName, emailAddress)
				}
			},
			onEmailAddressChange = { newEmailAddress ->
				scope.launch {
					candidateDataStoreViewModel.saveCandidateData(context, name, newEmailAddress)
				}
			},
			onRegisterClick = {
				if (isRegisterEnable){
					val candidate = Candidate(name= name, emailAddress = emailAddress)
					viewModel.insert(candidate)
					navController?.navigate(Screen.Test)
				}
			},
			onBackgroundTap = {
				keyboardController?.hide()
				focusManager.clearFocus()
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
	Column(
		modifier = Modifier
			.fillMaxSize()
			.background(color = md_theme_default_background)
			.padding(all = spacing_20)
			.pointerInput(Unit) {
				detectTapGestures(onTap = {onBackgroundTap()})
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

private fun isValidEmail(email : String) : Boolean {
	val emailRegex = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})"
	val pattern = Pattern.compile(emailRegex)
	return pattern.matcher(email).matches()
}



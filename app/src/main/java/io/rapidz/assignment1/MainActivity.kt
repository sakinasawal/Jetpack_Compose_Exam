package io.rapidz.assignment1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import io.rapidz.assignment1.viewmodel.SplashViewModel

val LocalNavController = compositionLocalOf<NavController> { error("No NavController provided") }

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

	override fun onCreate(savedInstanceState: Bundle?){
		super.onCreate(savedInstanceState)

		installSplashScreen()

		setContent {
			val splashViewModel : SplashViewModel = hiltViewModel()
			val keepSplashScreen by splashViewModel.keepSplashScreen.collectAsState()

			LaunchedEffect(keepSplashScreen) {
				installSplashScreen().setKeepOnScreenCondition { keepSplashScreen }
			}

			MainApplication()
		}
	}
}

@Composable
fun MainApplication(){
	val navController = rememberNavController()
	CompositionLocalProvider(LocalNavController provides navController){
		NavHost(navController = navController, startDestination = Screen.Role.route) {
			composable(Screen.Role)
			composable(Screen.Admin)
			composable(Screen.AdminHome)
			composable(Screen.AdminTest)
			composable(Screen.CandidateRegister)
			composable(Screen.Test)
		}
	}
}

@Preview(showBackground = true)
@Composable
fun MainApplicationPreview() {
	MainApplication()
}

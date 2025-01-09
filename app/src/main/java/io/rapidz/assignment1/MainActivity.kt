package io.rapidz.assignment1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.rapidz.assignment1.admin.AdminScreen
import io.rapidz.assignment1.candidate.CandidateScreen
import io.rapidz.assignment1.role.RoleSelectionScreen
import io.rapidz.assignment1.test.*
import io.rapidz.assignment1.viewmodel.SplashViewModel


class MainActivity : ComponentActivity() {

	private val splashViewModel : SplashViewModel by viewModels()
	override fun onCreate(savedInstanceState: Bundle?){
		super.onCreate(savedInstanceState)

		installSplashScreen().apply {
			setKeepOnScreenCondition{
				splashViewModel.keepSplashScreen.value
			}
		}

		setContent {
			MainApplication()
		}
	}
}

@Composable
fun MainApplication(){
	val navController = rememberNavController()
	NavHost(
		navController = navController,
		startDestination = Screen.Role.route)
	{
		composable(navController,Screen.Role)
		composable(navController,Screen.Admin)
		composable(navController,Screen.AdminHome)
		composable(navController,Screen.AdminTest)
		composable(navController,Screen.Candidate)
		composable(navController,Screen.Test)
	}
}

@Preview(showBackground = true)
@Composable
fun MainApplicationPreview() {
	MainApplication()
}

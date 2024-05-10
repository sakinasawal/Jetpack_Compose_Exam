package io.rapidz.assignment1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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

class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?){
		super.onCreate(savedInstanceState)

		installSplashScreen()

		setContent {
			MainApplication()
		}
	}
}

@Composable
fun MainApplication(){
	val navController = rememberNavController()
	NavHost(navController, startDestination = "role") {
		composable("role"){
			RoleSelectionScreen(navController)
		}
		composable("admin"){
			AdminScreen()
		}
		composable("candidate"){
			CandidateScreen(navController)
		}
		composable("test"){
			TestScreen(navController)
		}
		composable("test2"){
			TestScreen2(navController)
		}
		composable("test3"){
			TestScreen3()
		}
		composable("test4"){
			TestScreen4()
		}
	}
}

@Preview(showBackground = true)
@Composable
fun MainApplicationPreview() {
	MainApplication()
}

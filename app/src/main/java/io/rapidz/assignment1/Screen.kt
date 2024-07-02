package io.rapidz.assignment1

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import io.rapidz.assignment1.admin.AdminScreen
import io.rapidz.assignment1.candidate.CandidateScreen
import io.rapidz.assignment1.role.RoleSelectionScreen
import io.rapidz.assignment1.test.TestScreenBottomNav

sealed class Screen(val route: String, val content: @Composable (NavController) -> Unit) {

	data object Role : Screen("Role", { RoleSelectionScreen(navController = it) })

	data object Admin : Screen("Admin", { AdminScreen() })

	data object Candidate : Screen("Candidate", { CandidateScreen(navController = it) })

	data object Test : Screen("Test", { TestScreenBottomNav(navController = it) })

}

fun NavGraphBuilder.composable(navController: NavController, screen: Screen) {
	composable(screen.route) {
		screen.content(navController)
	}
}

/**
 * Navigate to the screen and remove all back stacks.
 */
fun NavController.navigate(screen: Screen) = navigate(screen.route) {}

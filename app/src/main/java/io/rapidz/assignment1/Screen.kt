package io.rapidz.assignment1

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import io.rapidz.assignment1.admin.AdminScreen
import io.rapidz.assignment1.candidate.CandidateScreen
import io.rapidz.assignment1.role.RoleSelectionScreen
import io.rapidz.assignment1.test.TestScreenBottomNav

sealed class Screen(val route: String, val content: @Composable (NavController, Bundle?) -> Unit) {

	data object Role : Screen("Role", { navController, _ -> RoleSelectionScreen(navController = navController) })

	data object Admin : Screen("Admin", { _, _ -> AdminScreen() })

	data object Candidate : Screen("Candidate", { navController, _ -> CandidateScreen(navController = navController) })

	data object Test : Screen("Test/{candidateId}", { navController, arguments ->
		val candidateId = arguments?.getString("candidateId")?.toLongOrNull()
		if (candidateId != null) {
			TestScreenBottomNav(navController = navController, candidateId = candidateId)
		} else {
			error("Candidate ID is required.")
		}
	})

}

fun NavGraphBuilder.composable(navController: NavController, screen: Screen) {
	composable(screen.route) {backStackEntry ->
		screen.content(navController, backStackEntry.arguments)
	}
}

/**
 * Navigate to the screen and remove all back stacks.
 */
fun NavController.navigate(screen: Screen) = navigate(screen.route) {}

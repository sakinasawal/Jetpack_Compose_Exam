package io.rapidz.assignment1

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import io.rapidz.assignment1.ui.admin.AdminHomeScreen
import io.rapidz.assignment1.ui.admin.AdminScreen
import io.rapidz.assignment1.ui.admin.AdminTestScreen
import io.rapidz.assignment1.ui.candidate.CandidateScreen
import io.rapidz.assignment1.ui.role.RoleSelectionScreen
import io.rapidz.assignment1.ui.test.TestScreen

sealed class Screen(val route: String, val content: @Composable (NavController, Bundle?) -> Unit) {

	data object Role : Screen("Role", { navController, _ -> RoleSelectionScreen(navController = navController) })

	data object Admin : Screen("Admin", { navController, _ -> AdminScreen(navController = navController) })

	data object AdminHome : Screen("AdminHome", { navController, _ -> AdminHomeScreen(navController = navController) })

	data object AdminTest : Screen("AdminTest/{candidateId}", { _, arguments ->
		val candidateId = arguments?.getString("candidateId")?.toLongOrNull()
		if (candidateId != null) {
			AdminTestScreen(candidateId = candidateId)
		} else {
			error("Candidate ID is required.")
		}
	})

	data object Candidate : Screen("Candidate", { navController, _ -> CandidateScreen(navController = navController) })

	data object Test : Screen("Test/{candidateId}?usePreviousData={usePreviousData}", { navController, arguments ->
		val candidateId = arguments?.getString("candidateId")?.toLongOrNull()
		val usePreviousData = arguments?.getString("usePreviousData")?.toBoolean() ?: false
		if (candidateId != null) {
			TestScreen(navController = navController, candidateId = candidateId, usePreviousData = usePreviousData)
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
fun NavController.navigate(screen: Screen) = navigate(screen.route)

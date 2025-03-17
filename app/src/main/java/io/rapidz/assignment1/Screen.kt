package io.rapidz.assignment1

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import io.rapidz.assignment1.ui.admin.AdminHomeScreen
import io.rapidz.assignment1.ui.admin.AdminScreen
import io.rapidz.assignment1.ui.admin.AdminTestScreen
import io.rapidz.assignment1.ui.candidate.CandidateRegisterScreen
import io.rapidz.assignment1.ui.role.RoleSelectionScreen
import io.rapidz.assignment1.ui.test.TestScreen

object Route {
	const val ROLE = "Role"
	const val ADMIN = "Admin"
	const val ADMIN_HOME = "AdminHome"
	const val ADMIN_TEST = "AdminTest"
	const val CANDIDATE_REGISTER = "Candidate_register"
	const val TEST = "Test"
}

object Key {
	const val CANDIDATE_ID = "candidateId"
	const val USE_PREVIOUS_DATA = "usePreviousData"
}

sealed class Screen(val route: String) {

	data object Role : Screen(Route.ROLE)
	data object Admin : Screen(Route.ADMIN)
	data object AdminHome : Screen(Route.ADMIN_HOME)

	data object AdminTest : Screen("${Route.ADMIN_TEST}/{${Key.CANDIDATE_ID}}"){
		fun createRoute(candidateId: Long) = "$route/$candidateId"
	}

	data object CandidateRegister : Screen(Route.CANDIDATE_REGISTER)

	data object Test : Screen("${Route.TEST}/{${Key.CANDIDATE_ID}}?${Key.USE_PREVIOUS_DATA}={${Key.USE_PREVIOUS_DATA}}"){
		fun createRoute(candidateId: Long, usePreviousData: Boolean) =
			"$route/$candidateId?${Key.USE_PREVIOUS_DATA}=$usePreviousData"
	}
}

fun NavGraphBuilder.composable(screen: Screen) {

	val arguments = when (screen) {
		is Screen.AdminTest -> listOf(navArgument(Key.CANDIDATE_ID) { type = NavType.LongType })
		is Screen.Test -> listOf(
			navArgument(Key.CANDIDATE_ID) { type = NavType.LongType },
			navArgument(Key.USE_PREVIOUS_DATA) { type = NavType.BoolType; defaultValue = false }
		)
		else -> emptyList()
	}

	composable(screen.route, arguments) { entry ->
		when(screen) {
			is Screen.Role -> RoleSelectionScreen()
			is Screen.Admin -> AdminScreen()
			is Screen.AdminHome -> AdminHomeScreen()

			is Screen.AdminTest -> {
				val candidateId = entry.arguments?.getLong(Key.CANDIDATE_ID)
				if (candidateId != null) {
					AdminTestScreen(candidateId = candidateId)
				} else {
					error("Candidate ID is required.")
				}
			}

			is Screen.CandidateRegister -> CandidateRegisterScreen()

			is Screen.Test -> {
				val candidateId = entry.arguments?.getLong(Key.CANDIDATE_ID)
				val usePreviousData = entry.arguments?.getBoolean(Key.USE_PREVIOUS_DATA) ?: false
				if (candidateId != null) {
					TestScreen()
				} else {
					error("Candidate ID is required.")
				}
			}
		}
	}
}

/**
 * Navigate to the screen and remove all back stacks.
 */
fun NavController.navigate(screen: Screen, candidateId: Long? = null, usePreviousData: Boolean? = null) {
	val route = when (screen) {
		is Screen.AdminTest -> screen.createRoute(candidateId ?: error("Candidate ID is required"))
		is Screen.Test -> screen.createRoute(candidateId ?: error("Candidate ID is required"), usePreviousData ?: false)
		else -> screen.route
	}
	navigate(route)
}

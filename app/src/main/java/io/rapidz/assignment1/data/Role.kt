package io.rapidz.assignment1.data

import io.rapidz.assignment1.utils.Constants.Role

data class Role(val role : String = Role.ROLE_ADMIN) {
	fun isCandidate(): Boolean = role.equals(Role.ROLE_CANDIDATE, ignoreCase = true)

	fun isAdmin(): Boolean = role.equals(Role.ROLE_ADMIN, ignoreCase = true)
}


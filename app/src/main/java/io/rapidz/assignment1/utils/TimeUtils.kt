package io.rapidz.assignment1.utils

object TimeUtils {

	const val MAX_SCORE = 100
	private const val SECOND_IN_MILLIS = 60000

	fun convertMillisToMinutes(millis: Long): Long {
		return millis/SECOND_IN_MILLIS
	}



}
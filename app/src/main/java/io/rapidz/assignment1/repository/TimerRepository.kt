package io.rapidz.assignment1.repository

import io.rapidz.assignment1.dao.TimerDao
import io.rapidz.assignment1.data.Timer
import javax.inject.Inject

class TimerRepository @Inject constructor(
	private val timerDao: TimerDao
) {
	suspend fun insertTimer(timer: Timer) {
		timerDao.insert(timer)
	}

	suspend fun getTimerByCandidateId(candidateId: Long): Timer? {
		return timerDao.getTimerByCandidateId(candidateId)
	}

	suspend fun updateTimer(timer: Timer) {
		timerDao.update(timer)
	}
}

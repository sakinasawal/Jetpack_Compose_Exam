package io.rapidz.assignment1.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import io.rapidz.assignment1.data.Timer
import io.rapidz.assignment1.repository.TimerRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class TimerViewModel @Inject constructor(private val timerRepository: TimerRepository) : ViewModel() {

	fun saveTimer(candidateId: Long, remainingTime: Int) {
		viewModelScope.launch {
			val existingTimer = timerRepository.getTimerByCandidateId(candidateId)
			if (existingTimer != null) {
				timerRepository.updateTimer(existingTimer.copy(remainingTime = remainingTime))
			} else {
				val newTimer = Timer(candidateId = candidateId, remainingTime = remainingTime)
				timerRepository.insertTimer(newTimer)
			}
		}
	}
}

class TimerViewModelFactory(private val timerRepository: TimerRepository) : ViewModelProvider.Factory {
	override fun <T : ViewModel> create(modelClass: Class<T>): T {
		if (modelClass.isAssignableFrom(TestViewModel::class.java)) {
			@Suppress("UNCHECKED_CAST")
			return TimerViewModel(timerRepository) as T
		}
		throw IllegalArgumentException("Unknown ViewModel class")
	}
}

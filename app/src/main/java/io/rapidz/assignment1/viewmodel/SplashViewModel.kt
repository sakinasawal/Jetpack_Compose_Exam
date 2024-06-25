package io.rapidz.assignment1.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SplashViewModel : ViewModel() {

	private val _keepSplashScreen = MutableStateFlow(true)
	val keepSplashScreen : StateFlow<Boolean> = _keepSplashScreen

	init {
	    viewModelScope.launch {
			delay(400)
			_keepSplashScreen.value = false
		}
	}
}
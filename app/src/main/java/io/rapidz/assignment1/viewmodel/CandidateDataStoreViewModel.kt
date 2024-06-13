package io.rapidz.assignment1.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import io.rapidz.assignment1.storage.DataStoreManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CandidateDataStoreViewModel(context: Context) : ViewModel() {
	private val _name = MutableStateFlow("")
	val name: StateFlow<String> = _name

	private val _email = MutableStateFlow("")
	val email: StateFlow<String> = _email

	init {
		viewModelScope.launch {
			DataStoreManager.getCandidateName(context).collect { value ->
				_name.value = value ?: ""
			}
		}
		viewModelScope.launch {
			DataStoreManager.getCandidateEmail(context).collect { value ->
				_email.value = value ?: ""
			}
		}
	}

	fun saveCandidateData(context: Context, name: String, email: String) {
		viewModelScope.launch {
			DataStoreManager.saveCandidateData(context, name, email)
		}
	}
}

class CandidateDataStoreViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
	override fun <T : ViewModel> create(modelClass: Class<T>): T {
		if (modelClass.isAssignableFrom(CandidateDataStoreViewModel::class.java)) {
			@Suppress("UNCHECKED_CAST")
			return CandidateDataStoreViewModel(context) as T
		}
		throw IllegalArgumentException("Unknown ViewModel class")
	}
}

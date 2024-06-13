package io.rapidz.assignment1.storage

import android.content.Context
import androidx.datastore.preferences.core.stringPreferencesKey

object PreferencesKeys {
	val NAME = stringPreferencesKey("name")
	val EMAIL_ADDRESS = stringPreferencesKey("email_address")
}
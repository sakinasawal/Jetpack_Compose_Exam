package io.rapidz.assignment1.storage

import androidx.datastore.preferences.core.Preferences

interface DataStoreInterface {
	suspend fun <T> writeToDataStore(key: Preferences.Key<T>, value: T)
	suspend fun writeMultipleToDataStore(vararg pairs: DataStoreValue<*>)
	suspend fun <T> readFromDataStore(key: Preferences.Key<T>): T?
}
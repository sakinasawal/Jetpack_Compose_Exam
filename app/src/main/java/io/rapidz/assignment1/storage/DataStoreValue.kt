package io.rapidz.assignment1.storage

import androidx.datastore.preferences.core.Preferences

sealed class DataStoreValue<T>(val key: Preferences.Key<T>, val value: T) {
	class StringValue(key: Preferences.Key<String>, value: String) : DataStoreValue<String>(key, value)
	class IntValue(key: Preferences.Key<Int>, value: Int) : DataStoreValue<Int>(key, value)
	class LongValue(key: Preferences.Key<Long>, value: Long) : DataStoreValue<Long>(key, value)
	class BooleanValue(key: Preferences.Key<Boolean>, value: Boolean) : DataStoreValue<Boolean>(key, value)
	class FloatValue(key: Preferences.Key<Float>, value: Float) : DataStoreValue<Float>(key, value)
}

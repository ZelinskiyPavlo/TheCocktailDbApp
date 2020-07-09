package com.test.thecocktaildb.dataNew.db.impl

import android.annotation.SuppressLint
import android.content.SharedPreferences
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.runBlocking


@Suppress("unused")
class SharedPrefsHelper(val sharedPreferences: SharedPreferences) {

    val all: Map<String, *>
        get() = sharedPreferences.all

    val editor: SharedPreferences.Editor
        get() = sharedPreferences.edit()

    fun getInt(key: String, defValue: Int = 0): Int {
        return sharedPreferences.getInt(key, defValue)
    }

    fun getBoolean(key: String, defValue: Boolean = false): Boolean {
        return sharedPreferences.getBoolean(key, defValue)
    }

    fun getLong(key: String, defValue: Long = 0L): Long {
        return sharedPreferences.getLong(key, defValue)
    }

    fun getDouble(key: String, defValue: Double = 0.0): Double {
        return java.lang.Double.longBitsToDouble(
            sharedPreferences.getLong(
                key,
                java.lang.Double.doubleToLongBits(defValue)
            )
        )
    }

    fun getFloat(key: String, defValue: Float = 0F): Float {
        return sharedPreferences.getFloat(key, defValue)
    }

    fun getString(key: String, defValue: String = ""): String {
        return sharedPreferences.getString(key, defValue) ?: defValue
    }

    fun putLong(key: String, value: Long) {
        commit { putLong(key, value) }
    }

    fun putInt(key: String, value: Int) {
        commit { putInt(key, value) }
    }

    fun putDouble(key: String, value: Double) {
        commit { putLong(key, java.lang.Double.doubleToRawLongBits(value)) }
    }

    fun putFloat(key: String, value: Float) {
        commit { putFloat(key, value) }
    }

    fun putBoolean(key: String, value: Boolean) {
        commit { putBoolean(key, value) }
    }

    fun putString(key: String, value: String?) {
        commit {
            if (value.isNullOrEmpty()) {
                remove(key)
            } else {
                putString(key, value)
            }
        }
    }

    fun remove(key: String) {
        commit { remove(key) }
    }

    fun contains(key: String): Boolean {
        return sharedPreferences.contains(key)
    }

    fun clear() {
        commit { clear() }
    }

    @SuppressLint("ApplySharedPref")
    inline fun commit(block: SharedPreferences.Editor.() -> Unit) {
        sharedPreferences
            .edit()
            .apply(block)
            .commit()
    }

    /**
     * Adds [SharedPreferences.OnSharedPreferenceChangeListener] to [SharedPreferences]
     * which listens to changes into it.
     *
     * @param key key to observe.
     * @param onChanged invokes when [SharedPreferences.OnSharedPreferenceChangeListener.onSharedPreferenceChanged] method is called.
     * Should returns as a result a refreshed data from database which will be offered to [callbackFlow].
     *
     * @return [Flow] of changed data.
     */
    @ExperimentalCoroutinesApi
    inline fun <R> observeChange(
        key: String,
        crossinline onChanged: suspend () -> R
    ): Flow<R> {
        return callbackFlow {
            val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, changedKey ->
                runBlocking {
                    if (key != changedKey) return@runBlocking

                    if (!isClosedForSend)
                        offer(onChanged())
                }
            }

            if (!isClosedForSend) {
                offer(onChanged())
            }

            sharedPreferences.registerOnSharedPreferenceChangeListener(listener)

            awaitClose {
                sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener)
            }
        }
    }

}
package com.test.preference.impl

import android.annotation.SuppressLint
import android.content.SharedPreferences
import javax.inject.Inject

@Suppress("unused")
class SharedPrefsHelper @Inject constructor(val sharedPreferences: SharedPreferences) {

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

    fun getString(key: String, defValue: String? = null): String? {
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
                this.putString(key, value)
            }
        }
    }

    inline fun <reified T: Any?> get(key: String, defaultValue: T): T {
        return when (T::class.java) {
            Boolean::class.java -> getBoolean(key, defValue = defaultValue as Boolean)
            String::class.java -> getString(key, defValue = defaultValue as? String?)
            Int::class.java -> getInt(key, defValue = defaultValue as Int)
            Long::class.java -> getLong(key, defValue = defaultValue as Long)
            Double::class.java -> getDouble(key, defValue = defaultValue as Double)
            else -> throw NotImplementedError("TODO implement")
        } as T
    }

    inline fun <reified T: Any?> set(key: String, value: T) {
        when (T::class.java) {
            Boolean::class.java -> putBoolean(key, value as Boolean)
            String::class.java -> putString(key, value as? String?)
            Int::class.java -> putInt(key, value as Int)
            Long::class.java -> putLong(key, value as Long)
            Double::class.java -> putDouble(key, value as Double)
            else -> throw NotImplementedError("TODO implement")
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> get(clazz: Class<T>, key: String, defaultValue: T): T {
        return when (clazz) {
            Boolean::class.java -> getBoolean(key, defValue = defaultValue as Boolean)
            String::class.java -> getString(key, defValue = defaultValue as? String?)
            Int::class.java -> getInt(key, defValue = defaultValue as Int)
            Long::class.java -> getLong(key, defValue = defaultValue as Long)
            Double::class.java -> getDouble(key, defValue = defaultValue as Double)
            else -> throw NotImplementedError("TODO implement")
        } as T
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> set(clazz: Class<T>, key: String, value: T) {
        when (clazz) {
            Boolean::class.java -> putBoolean(key, value as Boolean)
            String::class.java -> putString(key, value as? String?)
            Int::class.java -> putInt(key, value as Int)
            Long::class.java -> putLong(key, value as Long)
            Double::class.java -> putDouble(key, value as Double)
            else -> throw NotImplementedError("TODO implement")
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
    private inline fun commit(block: SharedPreferences.Editor.() -> Unit) {
        sharedPreferences
            .edit()
            .apply(block)
            .commit()
    }
}
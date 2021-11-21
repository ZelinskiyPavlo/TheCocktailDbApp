package com.test.preference.impl

import android.annotation.SuppressLint
import android.content.SharedPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.onCompletion
import javax.inject.Inject
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

@Suppress("unused")
class SharedPrefsHelper @Inject constructor(val sharedPreferences: SharedPreferences) {

    inline fun <reified T> observeKey(key: String, default: T): Flow<T> {
        val flow = MutableStateFlow(getItem(key, default))

        val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, changedKey ->
            if (key == changedKey) {
                flow.value = getItem(key, default)!!
            }
        }
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener)

        return flow
            .onCompletion { sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener) }
    }

    inline fun <reified T> getItem(key: String, default: T): T {
        @Suppress("UNCHECKED_CAST")
        return when (default) {
            is String -> getString(key, default) as T
            is Int -> getInt(key, default) as T
            is Long -> getLong(key, default) as T
            is Boolean -> getBoolean(key, default) as T
            is Float -> getFloat(key, default) as T
            else -> throw IllegalArgumentException("generic type not handle ${T::class.java.name}")
        }
    }

    inline fun <reified T> stateHandle(key: String, defaultValue: T):
            ReadWriteProperty<Any, T> =
        object : ReadWriteProperty<Any, T> {
            override fun getValue(thisRef: Any, property: KProperty<*>): T {
                return get(key, defaultValue)
            }

            override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
                set(key, value)
            }
        }

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

    inline fun <reified T> get(key: String, defaultValue: T): T {
        // TODO: 14.11.2021 Investigate further difference between class.javaObjectType i class.java
        return when (T::class.java) {
            Boolean::class.javaPrimitiveType, Boolean::class.javaObjectType ->
                getBoolean(key, defValue = defaultValue as Boolean)

            String::class.java -> getString(key, defValue = defaultValue as? String?)

            Int::class.javaPrimitiveType, Int::class.javaObjectType ->
                getInt(key, defValue = defaultValue as Int)

            Long::class.javaPrimitiveType, Long::class.javaObjectType ->
                getLong(key, defValue = defaultValue as Long)

            Double::class.javaPrimitiveType, Double::class.javaObjectType ->
                getDouble(key, defValue = defaultValue as Double)

            else -> throw NotImplementedError("TODO implement ${T::class.java}")
        } as T
    }

    inline fun <reified T : Any?> set(key: String, value: T) {
        when (T::class.java) {
            Boolean::class.javaPrimitiveType, Boolean::class.javaObjectType ->
                putBoolean(key, value as Boolean)

            String::class.java -> putString(key, value as? String?)

            Int::class.javaPrimitiveType, Int::class.javaObjectType ->
                putInt(key, value as Int)

            Long::class.javaPrimitiveType, Long::class.javaObjectType ->
                putLong(key, value as Long)

            Double::class.javaPrimitiveType, Double::class.javaObjectType ->
                putDouble(key, value as Double)

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
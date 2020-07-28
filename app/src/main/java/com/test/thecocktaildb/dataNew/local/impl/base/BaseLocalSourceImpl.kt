package com.test.thecocktaildb.dataNew.local.impl.base

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import com.test.thecocktaildb.dataNew.local.impl.SharedPrefsHelper

open class BaseLocalSourceImpl(
    val sharedPrefsHelper: SharedPrefsHelper
) {

    inline fun <reified T> sharedPrefLiveData(key: String, defaultValue: T): SharedPrefLiveData<T> {
        return SharedPrefLiveData(T::class.java, key, defaultValue)
    }

    inner class SharedPrefLiveData<T>(private val clazz: Class<T>,  val key: String, defaultValue: T) :
        MutableLiveData<T>(sharedPrefsHelper.get(clazz, key, defaultValue)) {

        private val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, sharedKey ->
            if (sharedKey == key) {
                val newValue = sharedPrefsHelper.get(clazz, key, defaultValue)
                if (newValue != value) setValue(newValue)
            }
        }

        override fun setValue(value: T) {
            super.setValue(value)
            if (value != this.value) sharedPrefsHelper.set(clazz, key, value)
        }

        override fun onActive() {
            sharedPrefsHelper.sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
            super.onActive()
        }

        override fun onInactive() {
            sharedPrefsHelper.sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener)
            super.onInactive()
        }
    }
}
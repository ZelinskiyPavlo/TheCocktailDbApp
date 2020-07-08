package com.test.thecocktaildb.util

import androidx.lifecycle.MutableLiveData
import com.test.thecocktaildb.ui.base.BaseViewModel
import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

inline fun <reified T> BaseViewModel.stateHandle(initialValue: T? = null, key: String? = null):
        ReadWriteProperty<Any, T?> =
    object : ReadWriteProperty<Any, T?> {
        override fun getValue(thisRef: Any, property: KProperty<*>): T? {
            val propertyKey = key ?: property.name
            return savedStateHandle[propertyKey]
        }

        override fun setValue(thisRef: Any, property: KProperty<*>, value: T?) {
            val propertyKey = key ?: property.name
            if (initialValue == null) savedStateHandle[propertyKey] = value
            else savedStateHandle[propertyKey] = initialValue
        }
    }

inline fun <reified T> BaseViewModel.liveDataStateHandle(
    initialValue: T? = null, key: String? = null
): ReadOnlyProperty<Any, MutableLiveData<T?>> =
    object : ReadOnlyProperty<Any, MutableLiveData<T?>> {
        override fun getValue(thisRef: Any, property: KProperty<*>): MutableLiveData<T?> {
            val propertyKey = key ?: property.name
            return if (initialValue == null) savedStateHandle.getLiveData(propertyKey)
            else savedStateHandle.getLiveData(propertyKey, initialValue)
        }
    }

package com.test.presentation.util

import com.test.presentation.ui.base.BaseViewModel
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

inline fun <reified T> BaseViewModel.stateHandle(key: String? = null, initialValue: T? = null):
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

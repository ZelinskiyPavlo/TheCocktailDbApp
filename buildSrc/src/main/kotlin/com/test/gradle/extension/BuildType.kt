package com.test.gradle.extension

import com.android.build.gradle.internal.dsl.BuildType

fun BuildType.buildConfigField(key: String, value: String) {
	buildConfigField("String", key, "\"$value\"")
}

fun BuildType.buildConfigField(key: String, value: Boolean) {
	buildConfigField("Boolean", key, value.toString())
}

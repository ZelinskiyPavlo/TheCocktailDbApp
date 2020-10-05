package com.test.gradle.extension

import com.android.builder.internal.BaseConfigImpl

fun <V> BaseConfigImpl.addManifestPlaceholders(vararg pairs: Pair<String, V>) =
	addManifestPlaceholders(if (pairs.isNotEmpty()) pairs.toMap(HashMap(pairs.size)) else emptyMap())

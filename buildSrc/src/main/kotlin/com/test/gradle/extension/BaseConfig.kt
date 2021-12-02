package com.test.gradle.extension

import com.android.builder.internal.BaseConfigImpl

fun BaseConfigImpl.addManifestPlaceholders(vararg pairs: Pair<String, Any>) =
	addManifestPlaceholders(if (pairs.isNotEmpty()) pairs.toMap(HashMap(pairs.size)) else emptyMap())

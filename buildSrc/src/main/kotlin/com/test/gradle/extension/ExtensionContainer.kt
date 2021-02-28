package com.test.gradle.extension

import org.gradle.api.plugins.ExtensionContainer

@Suppress("UNCHECKED_CAST", "EXTENSION_SHADOWED_BY_MEMBER")
internal fun <T> ExtensionContainer.getByName(name: String): T? = getByName(name) as? T?
package com.test.gradle.dependency

import org.gradle.kotlin.dsl.DependencyHandlerScope

fun DependencyHandlerScope.lifecycleDependencies() {

    "implementation"(Lib.AndroidX.Lifecycle.common)
    "implementation"(Lib.AndroidX.Lifecycle.extensions)
    "implementation"(Lib.AndroidX.Lifecycle.livedataKtx)
    "implementation"(Lib.AndroidX.Lifecycle.viewModelKtx)
}
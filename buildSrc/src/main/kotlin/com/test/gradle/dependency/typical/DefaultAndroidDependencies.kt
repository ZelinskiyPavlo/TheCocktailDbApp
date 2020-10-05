package com.test.gradle.dependency.typical

import org.gradle.kotlin.dsl.DependencyHandlerScope

fun DependencyHandlerScope.defaultAndroidDependencies() {

    defaultDependencies()

    "implementation"(Lib.AndroidX.coreKtx)
    "implementation"(Lib.AndroidX.collection)
    "implementation"(Lib.AndroidX.annotation)
}
package com.test.gradle.dependency.typical

import org.gradle.kotlin.dsl.DependencyHandlerScope

fun DependencyHandlerScope.defaultFeatureDependencies() {

    defaultAndroidDependencies()

    "implementation"(Lib.AndroidX.appCompat)
    "implementation"(Lib.AndroidX.fragment)
    "implementation"(Lib.AndroidX.fragmentKtx)
    "implementation"(Lib.AndroidX.legacy)
}

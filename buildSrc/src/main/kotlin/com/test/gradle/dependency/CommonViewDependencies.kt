package com.test.gradle.dependency

import org.gradle.kotlin.dsl.DependencyHandlerScope

fun DependencyHandlerScope.commonViewDependencies() {

    "implementation"(Lib.AndroidX.constraintlayout)
    "implementation"(Lib.AndroidX.recyclerView)
    "implementation"(Lib.Google.material)
}
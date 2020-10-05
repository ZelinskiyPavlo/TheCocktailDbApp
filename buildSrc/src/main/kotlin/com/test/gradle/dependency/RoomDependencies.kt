package com.test.gradle.dependency

import org.gradle.kotlin.dsl.DependencyHandlerScope

fun DependencyHandlerScope.roomDependencies() {

    "implementation"(Lib.AndroidX.Room.common)
    "kapt"(Lib.AndroidX.Room.compiler)
    "implementation"(Lib.AndroidX.Room.ktx)
    "implementation"(Lib.AndroidX.Room.runtime)
}
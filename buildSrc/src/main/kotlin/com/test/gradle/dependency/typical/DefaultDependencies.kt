package com.test.gradle.dependency.typical

import org.gradle.kotlin.dsl.DependencyHandlerScope

fun DependencyHandlerScope.defaultDependencies() {

    "implementation"(Lib.Logging.timber)
}
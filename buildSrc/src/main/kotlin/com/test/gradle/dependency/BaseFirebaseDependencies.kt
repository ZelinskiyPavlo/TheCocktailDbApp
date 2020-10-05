package com.test.gradle.dependency

import org.gradle.kotlin.dsl.DependencyHandlerScope

fun DependencyHandlerScope.baseFirebaseDependencies() {

    "implementation"(Lib.Firebase.analytics)
    "implementation"(Lib.Firebase.crashLytics)
    "implementation"(Lib.Firebase.remoteConfig)

}
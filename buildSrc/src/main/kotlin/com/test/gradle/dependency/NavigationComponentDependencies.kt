package com.test.gradle.dependency

import org.gradle.kotlin.dsl.DependencyHandlerScope

fun DependencyHandlerScope.navigationComponentDependencies() {

    "implementation"(Lib.AndroidX.Navigation.fragmentKtx)
    "implementation"(Lib.AndroidX.Navigation.uiKtx)
}
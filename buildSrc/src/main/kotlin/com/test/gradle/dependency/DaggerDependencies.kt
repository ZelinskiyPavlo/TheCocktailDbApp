package com.test.gradle.dependency

import org.gradle.kotlin.dsl.DependencyHandlerScope

fun DependencyHandlerScope.daggerDependencies() {

    "implementation"(Lib.Dagger.dagger)
    "kapt"(Lib.Dagger.daggerCompiler)
}

fun DependencyHandlerScope.daggerAndroidDependencies() {

    "implementation"(Lib.Dagger.DaggerAndroid.daggerAndroidSupport)
    "implementation"(Lib.Dagger.DaggerAndroid.daggerAndroid)
    "kapt"(Lib.Dagger.DaggerAndroid.daggerAndroidProcessor)

    "implementation"(Lib.Dagger.ComponentManager.componentManager)
}
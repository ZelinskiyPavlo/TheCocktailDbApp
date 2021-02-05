package com.test.gradle.dependency

import Lib
import org.gradle.kotlin.dsl.DependencyHandlerScope

fun DependencyHandlerScope.daggerKaptDependencies(daggerReflectProperty: Any?) {
    if (daggerReflectProperty == "true") {
        "lintChecks"("com.github.kokeroulis:dagger-reflect-lint:atsiap~module_with_generic_try_to_add_workaround_new_fixes-SNAPSHOT:0.1.0")
        "kapt"("com.github.kokeroulis:dagger-reflect-compiler:atsiap~module_with_generic_try_to_add_workaround_new_fixes-SNAPSHOT:0.1.0")
        "implementation"("com.github.kokeroulis:dagger-reflect:atsiap~module_with_generic_try_to_add_workaround_new_fixes-SNAPSHOT:0.1.0")
    } else {
        "kapt"(Lib.Dagger.daggerCompiler)
        "kapt"(Lib.Dagger.DaggerAndroid.daggerAndroidProcessor)
    }
}
import com.test.gradle.dependency.daggerDependencies
import com.test.gradle.dependency.lifecycleDependencies
import com.test.gradle.dependency.typical.defaultAndroidDependencies

plugins {
    `android-lib-module`
    `kotlin-kapt`
}

dependencies {
    implementation(dataLocal)

    defaultAndroidDependencies()
    daggerDependencies()

    lifecycleDependencies()
}
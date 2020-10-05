import com.test.gradle.dependency.daggerDependencies
import com.test.gradle.dependency.lifecycleDependencies
import com.test.gradle.dependency.roomDependencies
import com.test.gradle.dependency.typical.defaultAndroidDependencies
import com.test.gradle.dependency.typical.defaultDependencies

plugins {
    `android-lib-module`
    `kotlin-kapt`
}

dependencies {
    implementation(dataDatabase)

    defaultAndroidDependencies()

    roomDependencies()

    daggerDependencies()

    lifecycleDependencies()
}
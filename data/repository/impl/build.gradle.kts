import com.test.gradle.dependency.daggerDependencies
import com.test.gradle.dependency.lifecycleDependencies
import com.test.gradle.dependency.roomDependencies
import com.test.gradle.dependency.typical.defaultAndroidDependencies

plugins {
    `android-lib-module`
    `kotlin-kapt`
}

dependencies {
    implementation(dataRepository)
    implementation(dataDatabase)
    implementation(dataLocal)
    implementation(dataNetwork)

    defaultAndroidDependencies()

    roomDependencies()

    lifecycleDependencies()

    daggerDependencies()
}
import com.test.gradle.dependency.lifecycleDependencies
import com.test.gradle.dependency.typical.defaultAndroidDependencies
import com.test.gradle.dependency.typical.defaultDependencies

plugins {
    `android-lib-module`
}

dependencies {
    defaultAndroidDependencies()

    implementation(Lib.AndroidX.Room.common)

    lifecycleDependencies()
}
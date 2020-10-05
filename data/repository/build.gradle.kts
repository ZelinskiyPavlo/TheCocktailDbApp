import com.test.gradle.dependency.lifecycleDependencies
import com.test.gradle.dependency.typical.defaultAndroidDependencies

plugins {
    `android-lib-module`
}

dependencies {
    implementation(Lib.AndroidX.Room.common)

    defaultAndroidDependencies()

    lifecycleDependencies()
}
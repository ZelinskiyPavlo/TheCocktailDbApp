import com.test.gradle.dependency.daggerKaptDependencies

plugins {
    `android-lib-module`
    `kotlin-kapt`
}

dependencies {
    implementation(platformFirebase)

    api(Lib.Firebase.fcm)

    daggerKaptDependencies(properties["dagger.reflect"])
}
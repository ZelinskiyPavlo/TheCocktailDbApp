import com.test.gradle.dependency.daggerKaptDependencies

plugins {
    `android-lib-module`
    `kotlin-kapt`
}

dependencies {
    api(Lib.Firebase.analytics)
    api(Lib.Firebase.crashLytics)
    api(Lib.Firebase.remoteConfig)
    api(Lib.Firebase.dynamicLinks)

    daggerKaptDependencies(properties["dagger.reflect"])
}
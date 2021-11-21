import com.test.gradle.dependency.daggerKaptDependencies

plugins {
    `android-lib-module`
    `kotlin-kapt`
}

dependencies {
    api(platform(Lib.Firebase.bom))
    api(Lib.Firebase.analytics)
    api(Lib.Firebase.crashLytics)
    api(Lib.Firebase.remoteConfig)
    api(Lib.Firebase.dynamicLinks)

    // Without this dep dagger will throw error, see #1449 issue in google dagger repo
    implementation(coreDagger)
    daggerKaptDependencies(properties["dagger.reflect"])
}
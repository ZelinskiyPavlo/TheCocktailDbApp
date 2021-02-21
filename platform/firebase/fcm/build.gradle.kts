import com.test.gradle.dependency.daggerKaptDependencies

plugins {
    `android-lib-module`
    `kotlin-kapt`
}

dependencies {
    implementation(coreDagger)
    implementation(dataRepository)
    implementation(platformFirebase)
    implementation(localization)

    api(Lib.Firebase.fcm)

    daggerKaptDependencies(properties["dagger.reflect"])
}
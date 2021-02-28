import com.test.gradle.dependency.daggerKaptDependencies

plugins {
    `android-lib-module`
    `kotlin-kapt`
}

dependencies {
    implementation(coreCommon)
    implementation(coreDagger)
    implementation(dataRepository)
    implementation(dataDatabase)
    implementation(dataLocal)
    implementation(dataNetwork)

    implementation(Lib.AndroidX.Room.common)

    daggerKaptDependencies(properties["dagger.reflect"])

    implementation(Lib.AndroidX.annotation)
    implementation(Lib.AndroidX.Lifecycle.common)
    implementation(Lib.AndroidX.Lifecycle.livedataKtx)
}
import com.test.gradle.dependency.daggerKaptDependencies

plugins {
    `android-lib-module`
    `kotlin-kapt`
}

dependencies {
    implementation(coreDagger)
    implementation(dataLocal)

    daggerKaptDependencies(properties["dagger.reflect"])

    implementation(Lib.AndroidX.annotation)
    implementation(Lib.AndroidX.Lifecycle.common)
    implementation(Lib.AndroidX.Lifecycle.livedataKtx)
}
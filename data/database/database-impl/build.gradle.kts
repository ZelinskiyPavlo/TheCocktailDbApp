import com.test.gradle.dependency.daggerKaptDependencies

plugins {
    `android-lib-module`
    `kotlin-kapt`
}

dependencies {
    implementation(coreDagger)
    implementation(dataDatabase)

    api(Lib.AndroidX.Room.common)
    api(Lib.AndroidX.Room.ktx)
    api(Lib.AndroidX.Room.runtime)
    kapt(Lib.AndroidX.Room.compiler)

    daggerKaptDependencies(properties["dagger.reflect"])

    implementation(Lib.AndroidX.annotation)
    implementation(Lib.AndroidX.Lifecycle.common)
}
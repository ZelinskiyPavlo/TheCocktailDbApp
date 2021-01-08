plugins {
    `android-lib-module`
    `kotlin-kapt`
}

dependencies {
    implementation(dataDatabase)

    implementation(Lib.AndroidX.Room.common)
    kapt(Lib.AndroidX.Room.compiler)
    implementation(Lib.AndroidX.Room.ktx)
    implementation(Lib.AndroidX.Room.runtime)

    kapt(Lib.Dagger.daggerCompiler)
    implementation(Lib.Dagger.dagger)

    implementation(Lib.AndroidX.annotation)
    implementation(Lib.AndroidX.Lifecycle.common)
    implementation(Lib.AndroidX.Lifecycle.livedataKtx)
}
plugins {
    `android-lib-module`
    `kotlin-kapt`
}

dependencies {
    implementation(dataRepository)
    implementation(dataDatabase)
    implementation(dataLocal)
    implementation(dataNetwork)

    implementation(Lib.AndroidX.Room.common)

    kapt(Lib.Dagger.daggerCompiler)
    implementation(Lib.Dagger.dagger)

    implementation(Lib.AndroidX.annotation)
    implementation(Lib.AndroidX.Lifecycle.common)
    implementation(Lib.AndroidX.Lifecycle.livedataKtx)
}
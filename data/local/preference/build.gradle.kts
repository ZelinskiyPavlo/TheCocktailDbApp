plugins {
    `android-lib-module`
    `kotlin-kapt`
}

dependencies {
    implementation(dataLocal)

    kapt(Lib.Dagger.daggerCompiler)
    implementation(Lib.Dagger.dagger)

    implementation(Lib.AndroidX.annotation)
    implementation(Lib.AndroidX.Lifecycle.common)
    implementation(Lib.AndroidX.Lifecycle.livedataKtx)
}
plugins {
    `android-lib-module`
}

dependencies {
    implementation(coreCommon)

    implementation(Lib.AndroidX.Room.common)

    implementation(Lib.AndroidX.annotation)
    implementation(Lib.AndroidX.Lifecycle.common)
    implementation(Lib.AndroidX.Lifecycle.livedataKtx)
}
plugins {
    `feature-module`
}

dependencies {
    api(coreCommon)
    api(coreStyling)

    api(dataRepository)

    api(localization)

    implementation(platformFirebase)
    implementation(coreDagger)

    api(Lib.AndroidX.coreKtx)
    api(Lib.AndroidX.annotation)
    api(Lib.AndroidX.appCompat)
    api(Lib.AndroidX.fragmentKtx)
    api(Lib.AndroidX.legacy)
    api(Lib.AndroidX.constraintlayout)

    api(Lib.AndroidX.Lifecycle.common)
    api(Lib.AndroidX.Lifecycle.livedataKtx)
    api(Lib.AndroidX.Lifecycle.viewModelKtx)

    api(Lib.Logging.timber)

    implementation(Lib.AndroidX.recyclerView)
    implementation(Lib.Google.material)
    implementation(Lib.ImageLoading.Glide.glide)
    implementation(Lib.Icepick.icePick)
    kapt(Lib.Icepick.icePickProcessor)
}
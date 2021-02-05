plugins {
    `feature-module`
}

dependencies {
    api(coreCommon)
    api(coreStyling)
    api(coreCommonCocktail)

    api(dataRepository)

    api(localization)

    implementation(platformFirebase)
    implementation(coreDagger)

    // TODO: 08.01.2021 test only
    api("androidx.activity:activity:1.2.0-rc01")

    api(Lib.AndroidX.coreKtx)
    // TODO: 04.02.2021 check if this useful library
    api(Lib.AndroidX.collection)
    api(Lib.AndroidX.annotation)
    api(Lib.AndroidX.appCompat)
    api(Lib.AndroidX.fragmentKtx)
    api(Lib.AndroidX.legacy)
    api(Lib.AndroidX.constraintlayout)

    api(Lib.AndroidX.Lifecycle.common)
    // TODO: 04.02.2021 check if this useful library
    api(Lib.AndroidX.Lifecycle.extensions)
    api(Lib.AndroidX.Lifecycle.livedataKtx)
    api(Lib.AndroidX.Lifecycle.viewModelKtx)

    api(Lib.Logging.timber)

    implementation(Lib.AndroidX.recyclerView)
    implementation(Lib.Google.material)
    implementation(Lib.ImageLoading.Glide.glide)
    implementation(Lib.Icepick.icePick)
    kapt(Lib.Icepick.icePickProcessor)
}
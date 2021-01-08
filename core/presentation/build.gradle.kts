import com.test.gradle.dependency.daggerAndroidDependencies
import com.test.gradle.dependency.daggerDependencies
import com.test.gradle.dependency.navigationComponentDependencies

plugins {
    `feature-module`
}

dependencies {
    api(coreCommon)
    api(coreStyling)

    api(dataRepository)

    api(localization)

    implementation(platformFirebase)

    api(Lib.AndroidX.coreKtx)
    api(Lib.AndroidX.collection)
    api(Lib.AndroidX.annotation)
    api(Lib.AndroidX.appCompat)
    api(Lib.AndroidX.fragmentKtx)
    api(Lib.AndroidX.legacy)

    api(Lib.AndroidX.Lifecycle.common)
    api(Lib.AndroidX.Lifecycle.extensions)
    api(Lib.AndroidX.Lifecycle.livedataKtx)
    api(Lib.AndroidX.Lifecycle.viewModelKtx)

    api(Lib.Logging.timber)

    // TODO: 07.01.2021 потрібно такого позбутися, оскільки не зрозуміли, чи тут використовується
    //  implementation чи api
    daggerDependencies()
    daggerAndroidDependencies()

    navigationComponentDependencies()

    implementation(Lib.AndroidX.constraintlayout)
    implementation(Lib.AndroidX.recyclerView)
    implementation(Lib.Google.material)
    implementation(Lib.ImageLoading.Glide.glide)
    implementation(Lib.Icepick.icePick)
    kapt(Lib.Icepick.icePickProcessor)
}
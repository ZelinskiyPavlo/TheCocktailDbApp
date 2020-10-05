import com.test.gradle.dependency.*
import com.test.gradle.dependency.typical.defaultFeatureDependencies

plugins {
    `feature-module`
}

dependencies {
    implementation(coreCommon)
    implementation(dataRepository)

    implementation(coreStyling)

    implementation(localization)

    defaultFeatureDependencies()
    commonViewDependencies()

    daggerDependencies()
    daggerAndroidDependencies()

    navigationComponentDependencies()

    baseFirebaseDependencies()

    implementation(Lib.Icepick.icePick)
    kapt(Lib.Icepick.icePickProcessor)

    implementation(Lib.ImageLoading.Glide.glide)

}
import com.test.gradle.dependency.*
import com.test.gradle.dependency.typical.defaultFeatureDependencies

plugins {
    `feature-module`
}

dependencies {
    implementation(coreDagger)
    implementation(coreCommon)
    implementation(corePresentation)
    implementation(coreNavigation)
    implementation(dataRepository)

    implementation(platformFirebase)
    implementation(platformFirebaseFcm)

    defaultFeatureDependencies()

    daggerDependencies()
    daggerAndroidDependencies()

    lifecycleDependencies()

    navigationComponentDependencies()

    implementation(Lib.Firebase.analytics)
    implementation(Lib.Firebase.crashLytics)
    implementation(Lib.Firebase.remoteConfig)
    implementation(Lib.Firebase.dynamicLinks)
    implementation(Lib.Firebase.fcm)
}
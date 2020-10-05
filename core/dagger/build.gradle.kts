import com.test.gradle.dependency.daggerAndroidDependencies
import com.test.gradle.dependency.daggerDependencies

plugins {
    `android-lib-module`
    `kotlin-kapt`
}

dependencies {
    implementation(dataDatabase)
    implementation(dataLocal)
    implementation(dataNetwork)
    implementation(dataRepository)

    implementation(dataDatabaseImpl)
    implementation(dataLocalPreference)
    implementation(dataNetworkImpl)
    implementation(dataRepositoryImpl)

    implementation(platformFirebase)

    implementation(coreNavigation)
    implementation(coreNavigationImpl)

    daggerDependencies()
    daggerAndroidDependencies()

    // TODO: some library need to be deleted
    implementation(Lib.Network.Retrofit.retrofit)
    implementation(Lib.Network.Retrofit.converterGson)

    implementation(Lib.Firebase.analytics)
    implementation(Lib.Firebase.crashLytics)
    implementation(Lib.Firebase.remoteConfig)
}
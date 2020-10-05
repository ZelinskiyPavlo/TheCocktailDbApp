plugins {
    `android-lib-module`
}

dependencies {
    implementation(Lib.Firebase.analytics)
    implementation(Lib.Firebase.crashLytics)
    implementation(Lib.Firebase.remoteConfig)
    implementation(Lib.Firebase.fcm)
    implementation(Lib.Firebase.dynamicLinks)

    implementation(Lib.Dagger.dagger)
}
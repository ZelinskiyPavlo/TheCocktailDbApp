plugins {
    `android-lib-module`
}

dependencies {
    api(Lib.Firebase.analytics)
    api(Lib.Firebase.crashLytics)
    api(Lib.Firebase.remoteConfig)
    api(Lib.Firebase.dynamicLinks)
}
plugins {
    `android-lib-module`
}

dependencies {
    implementation(platformFirebase)

    api(Lib.Firebase.fcm)

    implementation(Lib.Dagger.dagger)
}
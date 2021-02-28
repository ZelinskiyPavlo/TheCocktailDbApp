plugins {
    `android-lib-module`
}

dependencies {
    api(Lib.Dagger.dagger)
    api(Lib.Dagger.DaggerAndroid.daggerAndroidSupport)
    api(Lib.Dagger.DaggerAndroid.daggerAndroid)
}
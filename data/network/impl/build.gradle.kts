plugins {
    `android-lib-module`
    `kotlin-kapt`
}

dependencies {
    implementation(coreCommon)

    implementation(dataNetwork)
    implementation(dataRepository)

    implementation(Lib.Network.Retrofit.retrofit)
    implementation(Lib.Network.Retrofit.converterGson)
    implementation(Lib.Network.Interceptor.loggingInterceptor)

    kapt(Lib.Dagger.daggerCompiler)
    implementation(Lib.Dagger.dagger)

    implementation(Lib.AndroidX.annotation)
    implementation(Lib.AndroidX.Lifecycle.common)
    implementation(Lib.AndroidX.Lifecycle.livedataKtx)
}
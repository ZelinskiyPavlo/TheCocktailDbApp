import com.test.gradle.dependency.daggerKaptDependencies

plugins {
    `android-lib-module`
    `kotlin-kapt`
}

dependencies {
    implementation(coreCommon)
    implementation(coreDagger)

    implementation(dataNetwork)
    implementation(dataRepository)


    api(Lib.Network.Retrofit.retrofit)
    api(Lib.Network.Retrofit.converterGson)
    debugApi(Lib.Network.Interceptor.loggingInterceptor)

    daggerKaptDependencies(properties["dagger.reflect"])

    implementation(Lib.AndroidX.coreKtx)
    implementation(Lib.AndroidX.annotation)
    implementation(Lib.AndroidX.Lifecycle.common)
    implementation(Lib.AndroidX.Lifecycle.livedataKtx)
}
import com.test.gradle.dependency.daggerDependencies
import com.test.gradle.dependency.typical.defaultAndroidDependencies

plugins {
    `android-lib-module`
    `kotlin-kapt`
}

dependencies {
    implementation(coreCommon)

    implementation(dataNetwork)
    implementation(dataRepository)

    defaultAndroidDependencies()
    daggerDependencies()

    implementation(Lib.Network.Retrofit.retrofit)
    implementation(Lib.Network.Retrofit.converterGson)
    implementation(Lib.Network.Interceptor.loggingInterceptor)

}
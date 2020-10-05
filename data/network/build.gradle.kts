import com.test.gradle.dependency.typical.defaultDependencies

plugins {
    `kotlin-module`
}

dependencies {
    defaultDependencies()

    implementation(Lib.Network.Retrofit.converterGson)
}
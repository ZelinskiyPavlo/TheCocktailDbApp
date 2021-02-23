import com.test.gradle.dependency.daggerKaptDependencies

plugins {
    `android-app-module`
    `kotlin-kapt`
    `google-services`
    `firebase-crashlytics`
}

kapt {
    correctErrorTypes = true
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    daggerKaptDependencies(properties["dagger.reflect"])

    implementation(Lib.Icepick.icePick)

    implementation(coreCommon)
    implementation(coreCommonCocktail)
    implementation(coreDagger)
    implementation(corePresentation)
    implementation(coreStyling)
    implementation(coreNavigation)
    implementation(localization)

    implementation(featureAuth)
    implementation(featureAuthLogin)
    implementation(featureAuthRegister)

    implementation(featureCocktail)
    implementation(featureTabHost)
    implementation(featureSearch)
    implementation(featureDetail)
    implementation(featureSetting)
    implementation(featureSettingCube)
    implementation(featureSettingProfile)
    implementation(featureSettingSeekBar)

    implementation(dataDatabase)
    implementation(dataLocal)
    implementation(dataNetwork)
    implementation(dataRepository)

    implementation(dataDatabaseImpl)
    implementation(dataLocalPreference)
    implementation(dataNetworkImpl)
    implementation(dataRepositoryImpl)

    implementation(platformFirebase)
    implementation(platformFirebaseFcm)
}

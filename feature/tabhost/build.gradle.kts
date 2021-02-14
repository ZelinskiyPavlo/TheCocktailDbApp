import com.test.gradle.dependency.daggerKaptDependencies

plugins {
    `feature-module`
}

dependencies {
    implementation(corePresentation)
    implementation(coreNavigation)
    implementation(coreDagger)

    implementation(featureCocktail)
    implementation(featureSetting)

    daggerKaptDependencies(properties["dagger.reflect"])

    implementation(Lib.Icepick.icePick)
    kapt(Lib.Icepick.icePickProcessor)
}
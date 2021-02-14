import com.test.gradle.dependency.daggerKaptDependencies

plugins {
    `feature-module`
}

dependencies {
    implementation(corePresentation)
    implementation(coreNavigation)
    implementation(coreDagger)

    implementation(featureSettingCube)
    implementation(featureSettingProfile)
    implementation(featureSettingSeekBar)

    daggerKaptDependencies(properties["dagger.reflect"])
}
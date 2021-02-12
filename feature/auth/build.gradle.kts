import com.test.gradle.dependency.daggerKaptDependencies

plugins {
    `feature-module`
}

dependencies {
    implementation(corePresentation)
    implementation(coreNavigation)
    implementation(coreDagger)

    implementation(featureAuthLogin)
    implementation(featureAuthRegister)

    daggerKaptDependencies(properties["dagger.reflect"])

    implementation(platformFirebase)
    implementation(platformFirebaseFcm)
}
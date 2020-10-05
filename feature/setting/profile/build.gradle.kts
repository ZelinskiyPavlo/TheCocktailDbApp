import com.test.gradle.dependency.*
import com.test.gradle.dependency.typical.defaultFeatureDependencies

plugins {
    `feature-module`
}

dependencies {
    implementation(featureSetting)

    implementation(coreDagger)
    implementation(coreNavigation)
    implementation(corePresentation)
    implementation(coreCommon)
    implementation(dataRepository)

    defaultFeatureDependencies()

    daggerDependencies()
    daggerAndroidDependencies()

    lifecycleDependencies()
    navigationComponentDependencies()

    baseFirebaseDependencies()
}
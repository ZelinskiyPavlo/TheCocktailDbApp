import com.test.gradle.dependency.daggerKaptDependencies

plugins {
    `feature-module`
}

dependencies {
    implementation(corePresentation)
    implementation(coreNavigation)
    implementation(coreDagger)
    implementation(coreCommonCocktail)

    implementation(platformFirebase)

    daggerKaptDependencies(properties["dagger.reflect"])
}
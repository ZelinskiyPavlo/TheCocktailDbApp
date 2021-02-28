plugins {
    `feature-module`
}

dependencies {
    implementation(corePresentation)
    implementation(coreDagger)
    implementation(coreNavigation)
    implementation(coreCommonCocktail)

    implementation(platformFirebase)
}
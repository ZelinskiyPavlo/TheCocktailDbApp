plugins {
    `feature-module`
}

dependencies {
    implementation(corePresentation)
    implementation(coreDagger)
    implementation(coreNavigation)

    implementation(platformFirebase)

    implementation(Lib.ImageLoading.Coil.coil)
}
plugins {
    `feature-module`
}

dependencies {
    implementation(corePresentation)
    implementation(coreDagger)
    implementation(coreNavigation)

    implementation(Lib.ImageLoading.Coil.coil)
    implementation(platformFirebase)
}
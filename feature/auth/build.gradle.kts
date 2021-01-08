import com.test.gradle.dependency.daggerAndroidDependencies
import com.test.gradle.dependency.daggerDependencies
import com.test.gradle.dependency.navigationComponentDependencies

plugins {
    `feature-module`
}

dependencies {
    implementation(corePresentation)
    implementation(coreDagger)
    implementation(coreNavigation)

    implementation(platformFirebase)
    implementation(platformFirebaseFcm)

    daggerDependencies()
    daggerAndroidDependencies()

    navigationComponentDependencies()
}
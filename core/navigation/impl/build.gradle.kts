import com.test.gradle.dependency.daggerDependencies
import com.test.gradle.dependency.navigationComponentDependencies

plugins {
    `feature-module`
    `navigation-components-safeargs`
}

dependencies {
    implementation(coreNavigation)

//    implementation(featureAuth)
//    implementation(featureAuthSplash)

    daggerDependencies()

    navigationComponentDependencies()
}
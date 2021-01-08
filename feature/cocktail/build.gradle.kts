import com.test.gradle.dependency.navigationComponentDependencies

plugins {
    `feature-module`
}

dependencies {
    implementation(corePresentation)

    navigationComponentDependencies()
}
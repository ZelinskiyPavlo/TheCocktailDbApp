import com.test.gradle.dependency.daggerAndroidDependencies
import com.test.gradle.dependency.daggerDependencies

plugins {
    `feature-module`
}

dependencies {
    implementation(corePresentation)
    implementation(coreDagger)

    daggerDependencies()
    daggerAndroidDependencies()
}
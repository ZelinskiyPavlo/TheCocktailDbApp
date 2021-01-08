import com.test.gradle.dependency.daggerAndroidDependencies
import com.test.gradle.dependency.daggerDependencies

plugins {
    `feature-module`
}

dependencies {
    api(corePresentation)
    implementation(coreDagger)

    daggerDependencies()
    daggerAndroidDependencies()
}
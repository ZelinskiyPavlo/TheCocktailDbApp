import com.test.gradle.dependency.daggerAndroidDependencies
import com.test.gradle.dependency.daggerDependencies
import com.test.gradle.dependency.lifecycleDependencies
import com.test.gradle.dependency.navigationComponentDependencies
import com.test.gradle.dependency.typical.defaultFeatureDependencies

plugins {
    `feature-module`
}

dependencies {
    implementation(coreDagger)
    implementation(corePresentation)
    implementation(dataRepository)

    defaultFeatureDependencies()

    daggerDependencies()
    daggerAndroidDependencies()

    lifecycleDependencies()

}
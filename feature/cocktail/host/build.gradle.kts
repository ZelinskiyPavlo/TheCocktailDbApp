import com.test.gradle.dependency.lifecycleDependencies
import com.test.gradle.dependency.navigationComponentDependencies
import com.test.gradle.dependency.typical.defaultAndroidDependencies
import com.test.gradle.dependency.typical.defaultDependencies
import com.test.gradle.dependency.typical.defaultFeatureDependencies

plugins {
    `feature-module`
}

dependencies {
    implementation(featureCocktail)
    implementation(corePresentation)

    defaultFeatureDependencies()

    lifecycleDependencies()

}
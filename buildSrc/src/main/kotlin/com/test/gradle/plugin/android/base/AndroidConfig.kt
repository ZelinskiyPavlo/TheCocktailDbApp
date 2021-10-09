package com.test.gradle.plugin.android.base

import Config
import com.android.build.gradle.BaseExtension
import com.android.build.gradle.internal.CompileOptions
import com.android.build.gradle.internal.dsl.BuildType
import com.android.build.gradle.internal.dsl.DefaultConfig
import com.android.build.gradle.internal.dsl.PackagingOptions
import com.android.build.gradle.internal.dsl.SigningConfig
import com.test.gradle.extension.addManifestPlaceholders
import com.test.gradle.extension.debug
import com.test.gradle.extension.getProperties
import com.test.gradle.extension.release
import org.gradle.api.Action
import org.gradle.api.JavaVersion
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project

internal inline val defaultConfigAction: Action<DefaultConfig>
	get() = Action {
		targetSdkVersion(Config.targetSdkVersion)
		minSdkVersion(Config.minSdkVersion)

		versionCode = Config.versionCode
		versionName = Config.versionName

		resConfigs(*Config.resConfigs)
		vectorDrawables.useSupportLibrary = true
	}

internal inline val Project.signInConfigAction: Action<NamedDomainObjectContainer<SigningConfig>>
	get() = Action {
		debug {
			configureSignIn(this, Config.Keystore.debugPropertiesFileName)
		}

		create(com.test.gradle.extension.RELEASE) {
			configureSignIn(this, Config.Keystore.releasePropertiesFileName)
		}
	}

internal inline val BaseExtension.buildTypesAction: Action<NamedDomainObjectContainer<BuildType>>
	get() = Action {
		debug {
//			signingConfig = signingConfigs.debug
			isMinifyEnabled = false
			isZipAlignEnabled = false

			addManifestPlaceholders(
				"exported" to true,
				"screenOrientation" to "unspecified"
			)
		}

		release {
//			signingConfig = signingConfigs.release
			isZipAlignEnabled = true

			@Suppress("ConstantConditionIf")
			if (Config.enableProguard) {
				isMinifyEnabled = true

				if (Config.proguardOptimization) {
					proguardFiles(getDefaultProguardFile(Config.proguardDefaultOpt), Config.proguardLocation)
				} else {
					proguardFiles(getDefaultProguardFile(Config.proguardDefault), Config.proguardLocation)
				}
			} else {
				isMinifyEnabled = false
			}

			addManifestPlaceholders(
				"exported" to false,
				"screenOrientation" to "portrait"
			)
		}
	}


internal fun BaseExtension.configureProductionFlavors() {

	val dimension = "version" /*any name*/

	flavorDimensions(dimension)
	productFlavors {
		create(com.test.gradle.extension.PRIMARY) {
			this.dimension = dimension
		}
		create(com.test.gradle.extension.SECONDARY) {
			this.dimension = dimension
			addManifestPlaceholders("appName" to "Flavor.${Config.appName}")
		}
	}
}

internal inline val compileOptionsAction: Action<CompileOptions>
	get() = Action {
		incremental = true
		sourceCompatibility = JavaVersion.VERSION_11
		targetCompatibility = JavaVersion.VERSION_11
	}

internal inline val packagingOptionsAction: Action<PackagingOptions>
	get() = Action {
		exclude("META-INF/impl_debug.kotlin_module")
//		exclude("META-INF/*.kotlin_module")
	}

private fun Project.configureSignIn(config: SigningConfig, propFileName: String): SigningConfig = config.apply {
	val prop = getProperties(Config.Keystore.dir + propFileName)

	storeFile = rootProject.file(Config.Keystore.dir + prop[Config.Keystore.Properties.storeFileName])
	storePassword = prop[Config.Keystore.Properties.storePassword] as String
	keyAlias = prop[Config.Keystore.Properties.keyAlias] as String
	keyPassword = prop[Config.Keystore.Properties.keyPassword] as String
}
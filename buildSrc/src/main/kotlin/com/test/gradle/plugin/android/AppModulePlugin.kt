package com.test.gradle.plugin.android

import com.android.build.gradle.AppExtension
import Config
import com.test.gradle.extension.addManifestPlaceholders
import com.test.gradle.extension.debug
import com.test.gradle.extension.release
import com.test.gradle.plugin.android.base.BaseAndroidModulePlugin
import org.gradle.api.plugins.PluginContainer

@Suppress("unused") // user in build.gradle.kts
class AppModulePlugin : BaseAndroidModulePlugin<AppExtension>() {

	override fun apply(plugins: PluginContainer): Unit = with(plugins) {
		apply("com.android.application")
		apply("kotlin-android")

		apply("com.google.gms.google-services")
		apply("com.google.firebase.crashlytics")
	}

	override fun configure(extension: AppExtension) = with(extension) {
		defaultConfig.applicationId = Config.applicationId
//		dataBinding.isEnabled = true
		buildFeatures.dataBinding = true

		buildTypes {
			debug {
				applicationIdSuffix = ".${Config.debugSuffix}"
				versionNameSuffix = "-${Config.debugSuffix}"
				isShrinkResources = false

				addManifestPlaceholders("appName" to ".${Config.appName} ${Config.debugSuffix}")
			}
			release {
				isShrinkResources = Config.enableProguard
				isCrunchPngs = true
				isMinifyEnabled = true
				addManifestPlaceholders("appName" to Config.appName)
			}
		}
	}
}
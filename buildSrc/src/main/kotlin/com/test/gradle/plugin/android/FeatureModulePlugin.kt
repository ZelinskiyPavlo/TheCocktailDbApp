package com.test.gradle.plugin.android

import com.android.build.gradle.LibraryExtension
import org.gradle.api.plugins.PluginContainer

@Suppress("unused") // user in build.gradle.kts
class FeatureModulePlugin : LibModulePlugin() {

	override fun apply(plugins: PluginContainer): Unit = with(plugins) {
		super.apply(plugins)
		apply("kotlin-kapt")
		apply("kotlin-android-extensions")
	}

	override fun configure(extension: LibraryExtension) {
		super.configure(extension)
//		extension.dataBinding.isEnabled = true
		extension.buildFeatures.dataBinding = true
	}
}
package com.test.gradle.plugin.android

import com.android.build.gradle.LibraryExtension
import com.test.gradle.plugin.android.base.BaseAndroidModulePlugin
import org.gradle.api.plugins.PluginContainer

@Suppress("unused") // used in build.gradle.kts
open class ResModulePlugin : BaseAndroidModulePlugin<LibraryExtension>() {

	override fun apply(plugins: PluginContainer): Unit = with(plugins) {
		apply("com.android.library")
	}

}
package com.test.gradle.plugin.android

import org.gradle.api.plugins.PluginContainer

@Suppress("unused") // used in build.gradle.kts
open class LibModulePlugin : ResModulePlugin() {

	override fun apply(plugins: PluginContainer): Unit = with(plugins) {
		super.apply(plugins)
		apply("kotlin-android")
	}

}
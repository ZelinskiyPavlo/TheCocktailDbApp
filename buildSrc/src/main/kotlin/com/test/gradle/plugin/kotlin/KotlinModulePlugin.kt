package com.test.gradle.plugin.kotlin

import com.test.gradle.extension.addKotlinStdLibDependency
import com.test.gradle.extension.setKotlinCompileOption
import org.gradle.api.Plugin
import org.gradle.api.Project

@Suppress("unused")
class KotlinModulePlugin : Plugin<Project> {

	override fun apply(project: Project) = with(project) {
		plugins.apply("kotlin")

		setKotlinCompileOption()
		addKotlinStdLibDependency("api")
	}
}

package com.test.gradle.plugin.android.base

import Config
import com.android.build.gradle.BaseExtension
import com.test.gradle.extension.addKotlinStdLibDependency
import com.test.gradle.extension.getByName
import com.test.gradle.extension.setKotlinCompileOption
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.PluginContainer

abstract class BaseAndroidModulePlugin<Extension : BaseExtension> : Plugin<Project> {

    final override fun apply(project: Project) = with(project) {
        apply(plugins)

        extensions.getByName<Extension>("android")?.apply {
            // Configure Extension specific build parameters.
            configure(this)

            // Configure common android build parameters.
            compileSdkVersion(Config.compileSdkVersion)
            buildToolsVersion(Config.buildToolsVersion)

            defaultConfig(defaultConfigAction)

//            signingConfigs(signInConfigAction)

            buildTypes(buildTypesAction)

//            configureProductionFlavors()

            compileOptions(compileOptionsAction)

            packagingOptions(packagingOptionsAction)

            setKotlinCompileOption()
        } ?: return

        // Adds required dependencies for all modules.
        addKotlinStdLibDependency("api")
    }

    protected abstract fun apply(plugins: PluginContainer)

    protected open fun configure(extension: Extension) = Unit

}


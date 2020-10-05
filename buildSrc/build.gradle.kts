plugins {
	`java-gradle-plugin`
	`kotlin-dsl`
}

gradlePlugin {
	plugins {
		// Use for ":app" module only
		// Applies "com.android.application" only
		register("android-app-module") {
			id = "android-app-module"
			implementationClass = "com.test.gradle.plugin.android.AppModulePlugin"
		}

		// Use for any android modules that do not contain any kotlin code
		// e.g. ":core:styling"
		// Applies "com.android.library" only
		register("android-res-module") {
			id = "android-res-module"
			implementationClass = "com.test.gradle.plugin.android.ResModulePlugin"
		}

		// Use for any android modules that does not need data binding or android extension
		// e.g. ":data:impl", ":network:impl"
		// Applies "com.android.library", "kotlin-android"
		register("android-lib-module") {
			id = "android-lib-module"
			implementationClass = "com.test.gradle.plugin.android.LibModulePlugin"
		}

		// Use for feature modules
		// e.g. ":feature:splash"
		// Applies "com.android.library", "kotlin-android", "kotlin-android-extensions"
		// Enables dataBinding
		register("feature-module") {
			id = "feature-module"
			implementationClass = "com.test.gradle.plugin.android.FeatureModulePlugin"
		}

		// Use for pure kotlin modules
		// Applies "kotlin" plugin
		register("kotlin-module") {
			id = "kotlin-module"
			implementationClass = "com.test.gradle.plugin.kotlin.KotlinModulePlugin"
		}
	}
}

dependencies {
	compileOnly(gradleApi())

	implementation("com.android.tools.build:gradle:4.0.0")
	implementation(kotlin("gradle-plugin", "1.4-M1"))
}

repositories {
	google()
	mavenCentral()
	jcenter()
	maven { url = uri("https://dl.bintray.com/kotlin/kotlin-eap") }
}

//buildscript {
//	repositories {
//		mavenCentral()
//		google()
//		jcenter()
//		gradlePluginPortal()
//		maven { url = uri("https://dl.bintray.com/kotlin/kotlin-eap") }
//	}
//
//	dependencies {
//		compileOnly(gradleApi())
//
//		classpath("com.android.tools.build:gradle:4.0.0")
//		classpath(kotlin("gradle-plugin", "1.4-M1"))
//		classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.3.0-rc01")
//
//		classpath("com.google.gms:google-services:4.3.3")
//		classpath("com.google.firebase:firebase-crashlytics-gradle:2.2.0")
//	}
//}

//allprojects {
//	repositories {
//		mavenCentral()
//		maven { url = uri("https://clojars.org/repo/") }
//		maven { url = uri("https://dl.bintray.com/kotlin/kotlin-eap") }
//		google()
//		jcenter()
//	}
//}


kotlinDslPluginOptions {
	@Suppress("UnstableApiUsage")
	experimentalWarning.set(false)
}



@Suppress("unused")
object Config {
	const val appName = "TheCocktailDb"
	const val appNameAlternate = "Pavlo Zelinskiy"
	const val applicationId = "com.test.thecocktaildb"
	const val compileSdkVersion = 29
	const val buildToolsVersion = "29.0.3"
	const val minSdkVersion = 21
	const val targetSdkVersion = 29
	const val versionCode = 1
	const val versionName = "1.0"

	const val debugSuffix = "debug"
	const val intentActionPrefix = "app.intent.action.open"
	val resConfigs = arrayOf("en", "uk", "ru")

	const val proguardDefault = "proguard-android.txt"
	const val proguardDefaultOpt = "proguard-android-optimize.txt"
	const val proguardLocation = "../proguard-rules.pro"
	const val enableProguard = false
	const val proguardOptimization = true

	const val enableCrashlytics = false

	const val jvmTarget = "1.8"

	object Keystore {
		const val dir = ".keystore/"
		const val debugPropertiesFileName = "debug.properties"
		const val releasePropertiesFileName = "release.properties"

		object Properties {
			const val storeFileName = "storeFileName"
			const val storePassword = "storePassword"
			const val keyAlias = "keyAlias"
			const val keyPassword = "keyPassword"
		}
	}
}
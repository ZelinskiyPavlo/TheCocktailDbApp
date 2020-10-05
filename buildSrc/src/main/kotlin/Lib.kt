object Version {

	const val kotlin = "1.4-M1"
	const val reflect = "1.4.0"
	const val coroutines = "1.3.3"

	object Plugin {
		const val android = "3.5.3"
		const val fabric = "1.31.2"
		const val crashlytics = "2.10.1"
	}

	object Network {
		const val retrofit = "2.9.0"
		const val gander = "3.1.0"
		const val loggingInterceptor = "4.5.0-RC1"
	}

	object Google {
		const val gson = "2.8.6"
		const val material = "1.1.0"
		const val autoService = "1.0-rc6"

		object PlayServices {
			const val base = "17.1.0"
			const val authApiPhone = "17.4.0"
		}
	}

	object Dagger {
		const val dagger = "2.25.2"

		object ComponentManager {
			const val componentManager = "2.1.0"
		}
	}

	object AndroidX {
		const val core = "1.3.0"
		const val appCompat = "1.1.0"
		const val annotation = "1.1.0"
		const val fragment = "1.3.0-alpha06"
		const val fragmentKtx = "1.2.5"
		const val recyclerView = "1.1.0"
		const val coordinatorLayout = "1.1.0"
		const val workManager = "2.3.2"
		const val constraintLayout = "1.1.3"
		const val swipeRefreshLayout = "1.0.0"
		const val preference = "1.1.0"
		const val collection = "1.1.0"
		const val legacy = "1.0.0"

		const val room = "2.2.5"

		const val lifecycle = "2.2.0"

		const val navigation = "2.3.0-rc01"

	}

	object Firebase {
		const val analytics = "17.4.4"
		const val crashLytics = "17.1.1"
		const val fcm = "20.2.4"
		const val dynamicLinks = "19.1.0"
		const val remoteConfig = "19.2.0"
	}

	object Logging {
		const val timber = "4.7.1"
	}

	object RxJava {
		const val rxjava = "2.2.19"
		const val rxandroid = "2.1.1"
		const val rxkotlin = "2.4.0"
	}

	object ImageLoading {
		object Glide {
			const val glide = "4.11.0"
		}

		object Coil {
			const val coil = "0.11.0"
		}
	}

	object Icepick {
		const val icePick = "3.2.0"
	}

	object Hyperion {
		const val hyperion = "0.9.27"
		const val hyperionAppInfo = "1.0.0"
		const val lynx = "1.6"
	}

}

object Lib {

	object Plugin {
		const val kotlin = "com.android.tools.build:gradle:${Version.Plugin.android}"
		const val android = "org.jetbrains.kotlin:kotlin-gradle-plugin${Version.kotlin}"
	}

	object Kotlin {
		const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib:$${Version.kotlin}"
		// TODO: 12.09.2020 may cause error because not used yet
		const val reflect = "org.jetbrains.kotlin:kotlin-reflect:${Version.reflect}"
		const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Version.coroutines}"
	}

	object AndroidX {
		const val coreKtx = "androidx.core:core-ktx:${Version.AndroidX.core}"
		const val appCompat = "androidx.appcompat:appcompat:${Version.AndroidX.appCompat}"
		const val annotation = "androidx.annotation:annotation:${Version.AndroidX.annotation}"
		const val recyclerView = "androidx.recyclerview:recyclerview:${Version.AndroidX.recyclerView}"
		const val fragment = "androidx.fragment:fragment:${Version.AndroidX.fragment}"
		const val fragmentKtx = "androidx.fragment:fragment-ktx:${Version.AndroidX.fragmentKtx}"
		const val constraintlayout = "androidx.constraintlayout:constraintlayout:${Version.AndroidX.constraintLayout}"
		const val workManager = "androidx.work:work-runtime-ktx:${Version.AndroidX.workManager}"
		const val preferenceKtx = "androidx.preference:preference-ktx:${Version.AndroidX.preference}"
		const val swiperefreshlayout = "androidx.swiperefreshlayout:swiperefreshlayout:${Version.AndroidX.swipeRefreshLayout}"
		const val collection = "androidx.collection:collection-ktx:${Version.AndroidX.collection}"
		const val legacy = "androidx.legacy:legacy-support-v4:${Version.AndroidX.legacy}"

		object Lifecycle {
			const val extensions = "androidx.lifecycle:lifecycle-extensions:${Version.AndroidX.lifecycle}"
			const val viewModelKtx = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Version.AndroidX.lifecycle}"
			const val livedataKtx = "androidx.lifecycle:lifecycle-livedata-ktx:${Version.AndroidX.lifecycle}"
			const val common = "androidx.lifecycle:lifecycle-common-java8:${Version.AndroidX.lifecycle}"
		}

		object Room {
			const val common = "androidx.room:room-common:${Version.AndroidX.room}"
			const val ktx = "androidx.room:room-ktx:${Version.AndroidX.room}"
			const val runtime = "androidx.room:room-runtime:${Version.AndroidX.room}"
			const val compiler = "androidx.room:room-compiler:${Version.AndroidX.room}"
		}

		object Navigation {
			const val fragmentKtx = "androidx.navigation:navigation-fragment-ktx:${Version.AndroidX.navigation}"
			const val uiKtx = "androidx.navigation:navigation-ui-ktx:${Version.AndroidX.navigation}"
		}
	}

	object Dagger {
		const val dagger = "com.google.dagger:dagger:${Version.Dagger.dagger}"
		const val daggerCompiler = "com.google.dagger:dagger-compiler:${Version.Dagger.dagger}"

		object DaggerAndroid {
			const val daggerAndroid = "com.google.dagger:dagger-android:${Version.Dagger.dagger}"
			const val daggerAndroidSupport = "com.google.dagger:dagger-android-support:${Version.Dagger.dagger}"
			const val daggerAndroidProcessor = "com.google.dagger:dagger-android-processor:${Version.Dagger.dagger}"
		}

		object ComponentManager {
			const val componentManager = "com.github.valeryponomarenko.componentsmanager:androidx:${Version.Dagger.ComponentManager.componentManager}"
		}
	}

	object Google {
		const val gson = "com.google.code.gson:gson:${Version.Google.gson}"
		const val material = "com.google.android.material:material:${Version.Google.material}"
		const val autoService = "com.google.auto.service:auto-service:${Version.Google.autoService}"

		object PlayServices {
			const val base = "com.google.android.gms:play-services-base:${Version.Google.PlayServices.base}"
			const val authApiPhone =
				"com.google.android.gms:play-services-auth-api-phone:${Version.Google.PlayServices.authApiPhone}"
		}
	}

	object Network {
		object Gander {
			const val gander = "com.ashokvarma.android:gander:${Version.Network.gander}"
			const val persintance = "com.ashokvarma.android:gander-persistence:${Version.Network.gander}"
		}

		object Retrofit {
			const val retrofit = "com.squareup.retrofit2:retrofit:${Version.Network.retrofit}"
			const val converterGson = "com.squareup.retrofit2:converter-gson:${Version.Network.retrofit}"
		}

		object Interceptor {
			const val loggingInterceptor = "com.squareup.okhttp3:logging-interceptor:${Version.Network.loggingInterceptor}"
		}
	}

	object RxJava {
		const val rxjava = "io.reactivex.rxjava2:rxjava:${Version.RxJava.rxjava}"
		const val rxandroid = "io.reactivex.rxjava2:rxandroid:${Version.RxJava.rxandroid}"
		const val rxkotlin = "io.reactivex.rxjava2:rxkotlin:${Version.RxJava.rxkotlin}"
	}

	object ImageLoading {
		object Glide {
			const val glide = "com.github.bumptech.glide:glide:${Version.ImageLoading.Glide.glide}"
		}

		object Coil {
			const val coil = "io.coil-kt:coil:${Version.ImageLoading.Coil.coil}"
		}
	}

	object Icepick {
		const val icePick = "frankiesardo:icepick:${Version.Icepick.icePick}"
		const val icePickProcessor = "frankiesardo:icepick-processor:${Version.Icepick.icePick}"
	}

	object Firebase {
		const val analytics = "com.google.firebase:firebase-analytics-ktx:${Version.Firebase.analytics}"
		const val crashLytics = "com.google.firebase:firebase-crashlytics:${Version.Firebase.crashLytics}"
		const val dynamicLinks = "com.google.firebase:firebase-dynamic-links-ktx:${Version.Firebase.dynamicLinks}"
		const val remoteConfig = "com.google.firebase:firebase-config-ktx:${Version.Firebase.remoteConfig}"
		const val fcm = "com.google.firebase:firebase-messaging:${Version.Firebase.fcm}"
	}
	
	object Logging {
		const val timber = "com.jakewharton.timber:timber:${Version.Logging.timber}"
	}

	object Hyperion {
		const val core = "com.willowtreeapps.hyperion:hyperion-core:${Version.Hyperion.hyperion}"
		const val attr = "com.willowtreeapps.hyperion:hyperion-attr:${Version.Hyperion.hyperion}"
		const val buildConfig = "com.willowtreeapps.hyperion:hyperion-build-config:${Version.Hyperion.hyperion}"
		const val disk = "com.willowtreeapps.hyperion:hyperion-disk:${Version.Hyperion.hyperion}"
		const val geigerCounter = "com.willowtreeapps.hyperion:hyperion-geiger-counter:${Version.Hyperion.hyperion}"
		const val measurement = "com.willowtreeapps.hyperion:hyperion-measurement:${Version.Hyperion.hyperion}"
		const val phoenix = "com.willowtreeapps.hyperion:hyperion-phoenix:${Version.Hyperion.hyperion}"
		const val preferences = "com.willowtreeapps.hyperion:hyperion-shared-preferences:${Version.Hyperion.hyperion}"
		const val plugin = "com.willowtreeapps.hyperion:hyperion-plugin:${Version.Hyperion.hyperion}"
		const val appinfo = "com.star_zero:hyperion-appinfo:${Version.Hyperion.hyperionAppInfo}"
		const val lynx = "com.github.pedrovgs:lynx:${Version.Hyperion.lynx}"
	}

}

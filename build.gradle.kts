buildscript {
    repositories {
        mavenCentral()
        google()
        jcenter()
        gradlePluginPortal()
        maven (url= "https://dl.bintray.com/kotlin/kotlin-eap")
    }
    dependencies {
        classpath ("com.android.tools.build:gradle:4.1.0")
        classpath ("org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.10")
        
        classpath ("com.google.gms:google-services:4.3.3")
        classpath ("com.google.firebase:firebase-crashlytics-gradle:2.2.0")
    }
}

allprojects {
    repositories {
        mavenCentral()
        maven (url= "https://clojars.org/repo/")
        maven (url= "https://clojars.org/repo/")
        maven (url= "https://dl.bintray.com/kotlin/kotlin-eap")
        maven (url= "https://jitpack.io")
        google()
        jcenter()
    }
}

subprojects {
    configurations.all {
        resolutionStrategy {
            force(
            )
        }
    }
}

tasks.register("clean", Delete::class) {
    project.allprojects.forEach {
        delete(it.buildDir)
    }
}
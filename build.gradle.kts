buildscript {
    repositories {
        mavenCentral()
        google()
        jcenter()
        gradlePluginPortal()
    }
    dependencies {
        classpath ("com.android.tools.build:gradle:7.0.2")
        classpath ("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.31")
        
        classpath ("com.google.gms:google-services:4.3.10")
        classpath ("com.google.firebase:firebase-crashlytics-gradle:2.7.1")
    }
}

plugins {
    id("com.osacky.doctor") version "0.7.3"
}

allprojects {
    repositories {
        mavenCentral()
        maven (url= "https://clojars.org/repo/")
        maven (url= "https://clojars.org/repo/")
        maven (url= "https://jitpack.io")
        google()
        jcenter()
    }
}

subprojects {
    configurations.all {
        resolutionStrategy {
            force(
                Lib.Force.javaClass.declaredFields.map { it.get(String()) as? String }.dropLast(1)
            )
        }
    }
}

tasks.register("clean", Delete::class) {
    project.allprojects.forEach {
        delete(it.buildDir)
    }
}
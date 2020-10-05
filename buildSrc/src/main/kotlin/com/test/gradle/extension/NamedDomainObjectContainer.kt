package com.test.gradle.extension

import org.gradle.api.NamedDomainObjectCollection

const val DEBUG = "debug"
const val RELEASE = "release"

const val PRIMARY = "primary"
const val SECONDARY = "secondary"

inline val <T> NamedDomainObjectCollection<T>.debug: T get() = getByName(DEBUG)

inline val <T> NamedDomainObjectCollection<T>.release: T get() = getByName(RELEASE)

fun <T> NamedDomainObjectCollection<T>.debug(config: T.() -> Unit): T = getByName(DEBUG, config)

fun <T> NamedDomainObjectCollection<T>.release(config: T.() -> Unit): T = getByName(RELEASE, config)

fun <T> NamedDomainObjectCollection<T>.primaryFlavor(config: T.() -> Unit): T = getByName(SECONDARY, config)
fun <T> NamedDomainObjectCollection<T>.secondaryFlavor(config: T.() -> Unit): T = getByName(
    SECONDARY, config)

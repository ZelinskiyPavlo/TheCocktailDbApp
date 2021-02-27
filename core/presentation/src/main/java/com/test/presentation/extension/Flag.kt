@file:Suppress("unused")

package com.test.presentation.extension

infix fun Int.containsFlag(flag: Int) = this or flag == this

infix fun Int.addFlag(flag: Int) = this.or(flag)

infix fun Int.toggleFlag(flag: Int) = this.xor(flag)

infix fun Int.removeFlag(flag: Int) = this.and(flag.inv())

fun combineFlags(vararg flags: Int) = flags.fold(0) { composedFlag, flag -> composedFlag addFlag flag }
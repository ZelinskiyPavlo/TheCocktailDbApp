package com.test.presentation.extension

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

fun Context.isAllPermissionGranted(vararg permissions: String): Boolean {
    return permissions.all {
        ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
    }
}

fun Context.checkPermissionGranted(vararg permissions: String, onResult: (granted: List<String>, notGranted: List<String>) -> Unit = { _, _ -> }): Boolean {
    val permissionGrantedMap = permissions.groupBy { ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED }

    onResult(
        permissionGrantedMap.getOrElse(true) { emptyList() },
        permissionGrantedMap.getOrElse(false) { emptyList() }
    )

    return permissionGrantedMap[false].isNullOrEmpty()
}
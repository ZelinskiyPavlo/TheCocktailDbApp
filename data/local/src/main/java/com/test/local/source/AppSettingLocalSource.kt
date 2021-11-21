package com.test.local.source

import kotlinx.coroutines.flow.Flow

interface AppSettingLocalSource {

    fun observeShowNavigationTitle(): Flow<Boolean>

    var showNavigationTitle: Boolean

    fun observeCurrentLanguage(): Flow<Int>

    var currentLanguage: Int

}
package com.test.repository.source

import kotlinx.coroutines.flow.Flow

interface AppSettingRepository {

    fun observeShowNavigationTitle(): Flow<Boolean>

    var showNavigationTitle: Boolean

    fun observeCurrentLanguage(): Flow<Int>

    var currentLanguage: Int

}
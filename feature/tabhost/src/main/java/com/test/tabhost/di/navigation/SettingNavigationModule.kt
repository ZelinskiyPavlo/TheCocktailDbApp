package com.test.tabhost.di.navigation

import com.test.setting.api.SettingNavigationApi
import com.test.tabhost.navigation.impl.SettingNavigationImpl
import dagger.Binds
import dagger.Module

@Module
internal interface SettingNavigationModule {

    @Suppress("unused")
    @Binds
    fun bindSettingNavigation(settingNavigationImpl: SettingNavigationImpl): SettingNavigationApi
}
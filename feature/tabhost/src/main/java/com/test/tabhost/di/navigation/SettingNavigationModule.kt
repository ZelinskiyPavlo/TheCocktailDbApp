package com.test.tabhost.di.navigation

import com.test.setting.navigation.SettingNavigationApi
import com.test.tabhost.navigation.impl.SettingNavigationImpl
import dagger.Binds
import dagger.Module

@Module
interface SettingNavigationModule {

    @Binds
    fun bindSettingNavigation(settingNavigationImpl: SettingNavigationImpl): SettingNavigationApi
}
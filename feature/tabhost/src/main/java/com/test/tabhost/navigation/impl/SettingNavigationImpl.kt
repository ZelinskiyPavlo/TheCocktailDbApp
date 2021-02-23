package com.test.tabhost.navigation.impl

import com.test.setting.api.SettingNavigationApi
import com.test.tabhost.api.TabHostNavigationApi
import javax.inject.Inject

class SettingNavigationImpl @Inject constructor(
    private val tabHostNavigationApi: TabHostNavigationApi
): SettingNavigationApi {

    override fun toProfile() {
        tabHostNavigationApi.toProfile()
    }

    override fun toCube() {
        tabHostNavigationApi.toCube()
    }

    override fun toSeekBar() {
        tabHostNavigationApi.toSeekBar()
    }

    override fun exit() {
        tabHostNavigationApi.exit()
    }
}
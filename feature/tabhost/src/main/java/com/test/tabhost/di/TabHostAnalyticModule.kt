package com.test.tabhost.di

import com.test.tabhost.analytic.TabHostAnalyticApi
import com.test.tabhost.analytic.TabHostAnalyticImpl
import dagger.Binds
import dagger.Module

@Module
interface TabHostAnalyticModule {

    @Suppress("unused")
    @Binds
    fun bindTabHostAnalytic(tabHostAnalyticImpl: TabHostAnalyticImpl): TabHostAnalyticApi
}
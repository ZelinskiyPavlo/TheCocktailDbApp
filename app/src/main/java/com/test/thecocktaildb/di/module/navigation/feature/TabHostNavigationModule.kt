package com.test.thecocktaildb.di.module.navigation.feature

import com.test.tabhost.api.TabHostNavigationApi
import com.test.thecocktaildb.feature.tabhost.TabHostNavigationImpl
import dagger.Binds
import dagger.Module

@Module
interface TabHostNavigationModule {

    @Suppress("unused")
    @Binds
    fun bindTabHostNavigation(tabHostNavigationImpl: TabHostNavigationImpl): TabHostNavigationApi
}
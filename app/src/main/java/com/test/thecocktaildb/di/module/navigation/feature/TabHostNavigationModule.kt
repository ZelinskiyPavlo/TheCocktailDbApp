package com.test.thecocktaildb.di.module.navigation.feature

import com.test.tabhost.navigation.api.TabHostNavigationApi
import com.test.thecocktaildb.navigation.feature.TabHostNavigationImpl
import dagger.Binds
import dagger.Module

@Module
interface TabHostNavigationModule {

    @Binds
    fun bindTabHostNavigation(tabHostNavigationImpl: TabHostNavigationImpl): TabHostNavigationApi
}
package com.test.thecocktaildb.di.module.navigation

import com.test.navigation.api.SimpleNavigatorApi
import com.test.thecocktaildb.navigation.SimpleNavigatorImpl
import dagger.Binds
import dagger.Module

@Module
interface SimpleNavigationModule {

    @Suppress("unused")
    @Binds
    fun bindSimpleNavigator(simpleNavigatorImpl: SimpleNavigatorImpl): SimpleNavigatorApi
}
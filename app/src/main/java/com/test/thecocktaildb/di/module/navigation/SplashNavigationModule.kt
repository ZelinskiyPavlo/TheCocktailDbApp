package com.test.thecocktaildb.di.module.navigation

import com.test.splash.navigation.SplashNavigationApi
import com.test.thecocktaildb.navigation.SplashNavigationImpl
import dagger.Binds
import dagger.Module

@Module
interface SplashNavigationModule {

    @Binds
    fun bindSplashNavigation(splashNavigationImpl: SplashNavigationImpl): SplashNavigationApi
}
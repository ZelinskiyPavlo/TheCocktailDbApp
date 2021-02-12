package com.test.thecocktaildb.di.module.navigation.feature

import com.test.splash.navigation.SplashNavigationApi
import com.test.thecocktaildb.navigation.feature.SplashNavigationImpl
import dagger.Binds
import dagger.Module

@Module
interface SplashNavigationModule {

    @Binds
    fun bindSplashNavigation(splashNavigationImpl: SplashNavigationImpl): SplashNavigationApi
}
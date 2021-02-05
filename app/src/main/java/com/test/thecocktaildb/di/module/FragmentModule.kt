package com.test.thecocktaildb.di.module

import com.test.auth.di.AuthFragmentModule
import com.test.auth.di.navigation.AuthCiceroneModule
import com.test.auth.ui.AuthHostFragment
import com.test.dagger.scope.PerFragment
import com.test.splash.ui.SplashFragment
import com.test.thecocktaildb.di.module.navigation.AuthNavigationModule
import com.test.thecocktaildb.di.module.navigation.SplashNavigationModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface FragmentModule {

    @Suppress("unused")
    @PerFragment
    @ContributesAndroidInjector(modules = [SplashNavigationModule::class])
    fun contributeSplashFragment(): SplashFragment

    @Suppress("unused")
    @PerFragment
    @ContributesAndroidInjector(modules = [AuthFragmentModule::class, AuthCiceroneModule::class, AuthNavigationModule::class])
    fun contributeAuthHostFragment(): AuthHostFragment
}
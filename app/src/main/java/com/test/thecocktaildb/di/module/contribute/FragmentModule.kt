package com.test.thecocktaildb.di.module

import com.test.auth.di.AuthFragmentModule
import com.test.auth.di.navigation.AuthCiceroneModule
import com.test.auth.ui.AuthHostFragment
import com.test.cube.ui.CubeFragment
import com.test.dagger.scope.PerFragment
import com.test.detail.ui.CocktailDetailsFragment
import com.test.profile.ui.ProfileFragment
import com.test.search.ui.SearchCocktailFragment
import com.test.seekbar.ui.RangeSeekBarFragment
import com.test.splash.ui.SplashFragment
import com.test.tabhost.di.TabHostFragmentModule
import com.test.tabhost.di.navigation.TabHostCiceroneModule
import com.test.tabhost.ui.TabHostFragment
import com.test.thecocktaildb.di.module.communication.DetailCommunicationModule
import com.test.thecocktaildb.di.module.communication.TabHostCommunicationModule
import com.test.thecocktaildb.di.module.navigation.SimpleNavigationModule
import com.test.thecocktaildb.di.module.navigation.feature.*
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
    @ContributesAndroidInjector(
        modules = [
            AuthFragmentModule::class, AuthCiceroneModule::class, AuthNavigationModule::class
        ]
    )
    fun contributeAuthHostFragment(): AuthHostFragment

    @Suppress("unused")
    @PerFragment
    @ContributesAndroidInjector(
        modules = [
            TabHostFragmentModule::class, TabHostCiceroneModule::class, TabHostNavigationModule::class,
            TabHostCommunicationModule::class
        ]
    )
    fun contributeTabHostFragment(): TabHostFragment

    @Suppress("unused")
    @PerFragment
    @ContributesAndroidInjector(modules = [SearchNavigationModule::class])
    fun contributeSearchFragment(): SearchCocktailFragment

    @Suppress("unused")
    @PerFragment
    @ContributesAndroidInjector(modules = [SimpleNavigationModule::class, DetailCommunicationModule::class])
    fun contributeDetailsFragment(): CocktailDetailsFragment

    @Suppress("unused")
    @PerFragment
    @ContributesAndroidInjector(modules = [ProfileNavigationModule::class])
    fun contributeProfileFragment(): ProfileFragment

    @Suppress("unused")
    @PerFragment
    @ContributesAndroidInjector
    fun contribureCubeFragment(): CubeFragment

    @Suppress("unused")
    @PerFragment
    @ContributesAndroidInjector
    fun contribureRangeSeekBarFragment(): RangeSeekBarFragment

}
package com.test.tabhost.di

import com.test.cocktail.di.CocktailAnalyticModule
import com.test.cocktail.di.CocktailFragmentModule
import com.test.cocktail.ui.fragment.CocktailFragment
import com.test.dagger.scope.PerNestedFragment
import com.test.setting.ui.SettingFragment
import com.test.tabhost.di.communication.CocktailCommunicationModule
import com.test.tabhost.di.navigation.CocktailNavigationModule
import com.test.tabhost.di.navigation.SettingNavigationModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface TabHostFragmentModule {

    @Suppress("unused")
    @PerNestedFragment
    @ContributesAndroidInjector(
        modules = [
            CocktailFragmentModule::class,
            CocktailNavigationModule::class,
            CocktailCommunicationModule::class,
            CocktailAnalyticModule::class]
    )
    fun provideCocktailFragment(): CocktailFragment

    @Suppress("unused")
    @PerNestedFragment
    @ContributesAndroidInjector(modules = [SettingNavigationModule::class])
    fun provideSettingFragment(): SettingFragment
}
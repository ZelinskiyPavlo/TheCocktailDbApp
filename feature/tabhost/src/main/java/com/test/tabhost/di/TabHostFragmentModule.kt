package com.test.tabhost.di

import com.test.cocktail.di.CocktailFragmentModule
import com.test.cocktail.ui.fragment.CocktailFragment
import com.test.dagger.scope.PerNestedFragment
import com.test.tabhost.di.navigation.CocktailNavigationModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface TabHostFragmentModule {

    @PerNestedFragment
    @ContributesAndroidInjector(modules = [CocktailFragmentModule::class, CocktailNavigationModule::class])
    fun provideCocktailFragment(): CocktailFragment

//    @PerNestedFragment
//    @ContributesAndroidInjector
//    fun provideSettingFragment(): SettingFragment
}
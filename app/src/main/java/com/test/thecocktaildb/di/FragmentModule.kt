package com.test.thecocktaildb.di

import com.test.thecocktaildb.cocktailDetailsScreen.CocktailDetailsFragment
import com.test.thecocktaildb.cocktailsScreen.CocktailsFragment
import com.test.thecocktaildb.searchCocktailsScreen.SearchCocktailsFragment
import com.test.thecocktaildb.splashScreen.SplashFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentModule {

    @ContributesAndroidInjector
    abstract fun contributeCocktailsFragment(): CocktailsFragment

    @ContributesAndroidInjector
    abstract fun contributeCocktalDetailsFragment(): CocktailDetailsFragment

    @ContributesAndroidInjector
    abstract fun contributeSearchCocktailsFragment(): SearchCocktailsFragment

    @ContributesAndroidInjector
    abstract fun contributeSplashFragment(): SplashFragment
}
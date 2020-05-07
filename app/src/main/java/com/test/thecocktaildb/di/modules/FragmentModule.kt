package com.test.thecocktaildb.di.modules

import com.test.thecocktaildb.cocktailDetailsScreen.CocktailDetailsFragment
import com.test.thecocktaildb.cocktailsScreen.CocktailsFragment
import com.test.thecocktaildb.searchCocktailsScreen.SearchCocktailsFragment
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
}
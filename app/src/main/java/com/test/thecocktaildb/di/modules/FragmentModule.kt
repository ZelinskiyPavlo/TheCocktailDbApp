package com.test.thecocktaildb.di.modules

import com.test.thecocktaildb.ui.cocktailDetailsScreen.CocktailDetailsFragment
import com.test.thecocktaildb.ui.cocktailsScreen.CocktailFilterFragment
import com.test.thecocktaildb.ui.cocktailsScreen.CocktailsFragment
import com.test.thecocktaildb.ui.searchCocktailsScreen.SearchCocktailsFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentModule {

    @ContributesAndroidInjector
    @Suppress("unused")
    abstract fun contributeCocktailsFragment(): CocktailsFragment

    @ContributesAndroidInjector
    @Suppress("unused")
    abstract fun contributeCocktailDetailsFragment(): CocktailDetailsFragment

    @ContributesAndroidInjector
    @Suppress("unused")
    abstract fun contributeSearchCocktailsFragment(): SearchCocktailsFragment

    @ContributesAndroidInjector
    @Suppress("unused")
    abstract fun contributeFilterFragment(): CocktailFilterFragment
}
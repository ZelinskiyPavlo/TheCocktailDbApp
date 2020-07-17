package com.test.thecocktaildb.di.modules

import com.test.thecocktaildb.ui.cocktail.CocktailsActivity
import com.test.thecocktaildb.ui.cocktail.favorite.FavoriteFragment
import com.test.thecocktaildb.ui.cocktail.filter.CocktailFilterFragment
import com.test.thecocktaildb.ui.cocktail.history.CocktailsFragment
import com.test.thecocktaildb.ui.cocktail.host.HostFragment
import com.test.thecocktaildb.ui.detail.CocktailDetailsFragment
import com.test.thecocktaildb.ui.profile.ProfileFragment
import com.test.thecocktaildb.ui.search.SearchCocktailsFragment
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

    @ContributesAndroidInjector
    @Suppress("unused")
    abstract fun contributeHostFragment(): HostFragment

    @ContributesAndroidInjector
    @Suppress("unused")
    abstract fun contributeFavoriteFragment(): FavoriteFragment

    @ContributesAndroidInjector
    @Suppress("unused")
    abstract fun contributeProfileFragment(): ProfileFragment

    @ContributesAndroidInjector
    @Suppress("unused")
    abstract fun contributeCocktailsActivity(): CocktailsActivity
}
package com.test.thecocktaildb.di.module

import com.test.thecocktaildb.ui.auth.AuthActivity
import com.test.thecocktaildb.ui.cocktail.MainActivity
import com.test.thecocktaildb.ui.cocktail.favorite.FavoriteFragment
import com.test.thecocktaildb.ui.cocktail.filter.CocktailFilterFragment
import com.test.thecocktaildb.ui.cocktail.history.HistoryFragment
import com.test.thecocktaildb.ui.cocktail.host.HostFragment
import com.test.thecocktaildb.ui.detail.CocktailDetailsFragment
import com.test.thecocktaildb.ui.profile.ProfileFragment
import com.test.thecocktaildb.ui.search.SearchCocktailFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentModule {

    @ContributesAndroidInjector
    @Suppress("unused")
    abstract fun contributeCocktailsFragment(): HistoryFragment

    @ContributesAndroidInjector
    @Suppress("unused")
    abstract fun contributeCocktailDetailsFragment(): CocktailDetailsFragment

    @ContributesAndroidInjector
    @Suppress("unused")
    abstract fun contributeSearchCocktailsFragment(): SearchCocktailFragment

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
    abstract fun contributeMainActivity(): MainActivity

    @ContributesAndroidInjector
    @Suppress("unused")
    abstract fun contributeAuthActivity(): AuthActivity
}
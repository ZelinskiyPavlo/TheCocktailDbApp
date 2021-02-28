package com.test.cocktail.di

import com.test.cocktail.ui.fragment.favorite.FavoriteFragment
import com.test.cocktail.ui.fragment.filter.FilterFragment
import com.test.cocktail.ui.fragment.history.HistoryFragment
import com.test.dagger.scope.PerDeeplyNestedFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface CocktailFragmentModule {

    @Suppress("unused")
    @PerDeeplyNestedFragment
    @ContributesAndroidInjector
    fun contributeHistoryFragment(): HistoryFragment

    @Suppress("unused")
    @PerDeeplyNestedFragment
    @ContributesAndroidInjector
    fun contributeFavoriteFragment(): FavoriteFragment

    @Suppress("unused")
    @PerDeeplyNestedFragment
    @ContributesAndroidInjector
    fun contributeFilterFragment(): FilterFragment
}
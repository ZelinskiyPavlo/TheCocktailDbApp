package com.test.cocktail.di

import com.test.cocktail.ui.fragment.favorite.FavoriteFragment
import com.test.cocktail.ui.fragment.filter.FilterFragment
import com.test.cocktail.ui.fragment.history.HistoryFragment
import com.test.dagger.scope.PerDeeplyNestedFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface CocktailFragmentModule {

    @PerDeeplyNestedFragment
    @ContributesAndroidInjector
    fun contribureHistoryFragment(): HistoryFragment

    @PerDeeplyNestedFragment
    @ContributesAndroidInjector
    fun contribureFavoriteFragment(): FavoriteFragment

    @PerDeeplyNestedFragment
    @ContributesAndroidInjector
    fun contribureFilterFragment(): FilterFragment
}
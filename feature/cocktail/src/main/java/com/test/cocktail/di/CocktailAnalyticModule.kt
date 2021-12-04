package com.test.cocktail.di

import com.test.cocktail.analytic.CocktailAnalyticApi
import com.test.cocktail.analytic.CocktailAnalyticImpl
import dagger.Binds
import dagger.Module

@Module
interface CocktailAnalyticModule {

    @Suppress("unused")
    @Binds
    fun bindCocktailAnalytic(cocktailAnalyticsImpl: CocktailAnalyticImpl): CocktailAnalyticApi
}
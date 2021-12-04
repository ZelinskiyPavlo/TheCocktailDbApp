package com.test.detail.di

import com.test.detail.analytic.CocktailDetailsAnalyticApi
import com.test.detail.analytic.CocktailDetailsAnalyticImpl
import dagger.Binds
import dagger.Module

@Module
interface CocktailDetailsAnalyticModule {

    @Suppress("unused")
    @Binds
    fun bindCocktailDetailsAnalytic(CocktailDetailsAnalyticsImpl: CocktailDetailsAnalyticImpl): CocktailDetailsAnalyticApi
}
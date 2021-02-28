package com.test.tabhost.di.navigation

import com.test.cocktail.api.CocktailNavigationApi
import com.test.tabhost.navigation.impl.CocktailNavigationImpl
import dagger.Binds
import dagger.Module

@Module
internal interface CocktailNavigationModule {

    @Suppress("unused")
    @Binds
    fun bindCocktailNavigationApi(cocktailNavigationImpl: CocktailNavigationImpl): CocktailNavigationApi
}
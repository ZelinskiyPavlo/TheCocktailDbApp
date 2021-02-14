package com.test.tabhost.di.navigation

import com.test.cocktail.navigation.CocktailNavigationApi
import com.test.tabhost.navigation.impl.CocktailNavigationImpl
import dagger.Binds
import dagger.Module

@Module
interface CocktailNavigationModule {

    @Binds
    fun bindCocktailNavigationApi(cocktailNavigationImpl: CocktailNavigationImpl): CocktailNavigationApi
}
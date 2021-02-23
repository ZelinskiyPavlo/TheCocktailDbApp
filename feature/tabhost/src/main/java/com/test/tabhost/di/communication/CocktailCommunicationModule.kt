package com.test.tabhost.di.communication

import com.test.cocktail.api.CocktailCommunicationApi
import com.test.tabhost.communication.CocktailCommunicationImpl
import dagger.Binds
import dagger.Module

@Module
interface CocktailCommunicationModule {

    @Suppress("unused")
    @Binds
    fun bindCocktailCommunication(
        cocktailCommunicationImpl: CocktailCommunicationImpl
    ): CocktailCommunicationApi
}
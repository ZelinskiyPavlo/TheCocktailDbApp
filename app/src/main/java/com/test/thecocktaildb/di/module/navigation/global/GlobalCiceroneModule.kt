package com.test.thecocktaildb.di.module.navigation.global

import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Router
import com.test.thecocktaildb.di.DiConstant
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

@Module
class GlobalCiceroneModule {

    private val cicerone: Cicerone<Router> = Cicerone.create()

    @Provides
    @Singleton
    @Named(DiConstant.GLOBAL)
    fun provideRouter() = cicerone.router

    @Provides
    @Singleton
    @Named(DiConstant.GLOBAL)
    fun provideNavigatorHolder() = cicerone.getNavigatorHolder()

}
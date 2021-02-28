package com.test.tabhost.di.navigation

import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import com.test.dagger.scope.PerFragment
import dagger.Module
import dagger.Provides

@Module
class TabHostCiceroneModule {

    val cicerone = Cicerone.create()

    @Provides
    @PerFragment
    fun provideNavigationHolder(): NavigatorHolder {
        return cicerone.getNavigatorHolder()
    }

    @Provides
    @PerFragment
    fun provideRouter(): Router {
        return cicerone.router
    }
}
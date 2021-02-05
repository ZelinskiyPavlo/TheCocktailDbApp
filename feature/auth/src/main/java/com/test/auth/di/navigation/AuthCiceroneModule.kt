package com.test.auth.di.navigation

import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import com.test.dagger.scope.PerFragment
import dagger.Module
import dagger.Provides

@Module
class AuthCiceroneModule {

    private val cicerone = Cicerone.create()

    @Provides
    @PerFragment
    fun provideCiceroneNavigatorHolder(): NavigatorHolder {
        return cicerone.getNavigatorHolder()
    }

    @Provides
    @PerFragment
    fun provideCiceroneRouter(): Router {
        return cicerone.router
    }

}
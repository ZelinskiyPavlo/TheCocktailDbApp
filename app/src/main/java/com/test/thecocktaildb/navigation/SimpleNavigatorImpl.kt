package com.test.thecocktaildb.navigation

import com.github.terrakok.cicerone.Router
import com.test.navigation.api.SimpleNavigatorApi
import com.test.thecocktaildb.di.DiConstant
import javax.inject.Inject
import javax.inject.Named

class SimpleNavigatorImpl @Inject constructor(
    @param:Named(DiConstant.GLOBAL) private val router: Router
): SimpleNavigatorApi {

    override fun exit() {
        router.exit()
    }
}
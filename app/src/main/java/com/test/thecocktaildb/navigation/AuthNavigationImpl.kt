package com.test.thecocktaildb.navigation

import com.github.terrakok.cicerone.Router
import com.test.auth.navigation.outer.AuthNavigationApi
import com.test.thecocktaildb.di.DiConstant
import javax.inject.Inject
import javax.inject.Named

// TODO: 02.02.2021 maybe create BaseHostNavigation where i Inject Global router???
class AuthNavigationImpl @Inject constructor(
    @param:Named(DiConstant.GLOBAL) private val router: Router
) : AuthNavigationApi {

    override fun toCocktail() {
        TODO("Not yet implemented")
    }
}
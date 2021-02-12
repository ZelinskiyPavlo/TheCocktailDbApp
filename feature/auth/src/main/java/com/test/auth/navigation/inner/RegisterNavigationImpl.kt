package com.test.auth.navigation.inner

import com.github.terrakok.cicerone.Router
import com.test.auth.navigation.outer.AuthNavigationApi
import com.test.register.navigation.RegisterNavigationApi
import javax.inject.Inject

class RegisterNavigationImpl @Inject constructor(
    private val outerNavigationApi: AuthNavigationApi,
    private val innerNavigationRouter: Router
): RegisterNavigationApi {

    override fun toTabHost() {
        outerNavigationApi.toTabHost()
    }

    override fun toLogin() {
        innerNavigationRouter.exit()
    }
}
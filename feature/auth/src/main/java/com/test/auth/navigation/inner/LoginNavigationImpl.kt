package com.test.auth.navigation.inner

import com.github.terrakok.cicerone.Router
import com.test.auth.api.AuthNavigationApi
import com.test.auth.navigation.routing.Screen
import com.test.login.api.LoginNavigationApi
import javax.inject.Inject

internal class LoginNavigationImpl @Inject constructor(
    private val outerNavigationApi: AuthNavigationApi,
    private val innerNavigationRouter: Router
): LoginNavigationApi {

    override fun toTabHost() {
        outerNavigationApi.toTabHost()
    }

    override fun toRegister() {
        innerNavigationRouter.navigateTo(Screen.register(), false)
    }
}
package com.test.auth.navigation.inner

import com.github.terrakok.cicerone.Router
import com.test.auth.navigation.outer.AuthNavigationApi
import com.test.auth.navigation.routing.Screen
import com.test.login.navigation.LoginNavigationApi
import javax.inject.Inject

// TODO: 02.02.2021 toTabHost needs to be extracted to higher lever class and then inherited
class LoginNavigationImpl @Inject constructor(
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
package com.test.impl.auth

import androidx.navigation.NavController
import com.test.navigation.auth.LoginNavigator
import com.test.thecocktaildb.presentation.ui.auth.login.LoginFragmentDirections

class LoginNavigatorImpl : LoginNavigator {

    override fun toMainActivity(
        navController: NavController,
        notificationType: String?,
        cocktailId: String?
    ) {
        val action = LoginFragmentDirections
            .actionLoginFragmentToMainActivity(notificationType, cocktailId)
        navController.navigate(action)
    }

    override fun toRegisterFragment(navController: NavController) {
        val action = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
        navController.navigate(action)
    }
}
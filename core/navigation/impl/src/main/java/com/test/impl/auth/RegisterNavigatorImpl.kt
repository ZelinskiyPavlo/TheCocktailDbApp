package com.test.impl.auth

import androidx.navigation.NavController
import com.test.navigation.auth.RegisterNavigator
import com.test.thecocktaildb.presentation.ui.auth.register.RegisterFragmentDirections

class RegisterNavigatorImpl : RegisterNavigator {

    override fun toMainActivity(
        navController: NavController,
        notificationType: String?,
        cocktailId: String?
    ) {
        val action = RegisterFragmentDirections
            .actionRegisterFragmentToMainActivity(notificationType, cocktailId)
        navController.navigate(action)
    }

    override fun toLoginFragment(navController: NavController) {
        navController.popBackStack()
    }

}
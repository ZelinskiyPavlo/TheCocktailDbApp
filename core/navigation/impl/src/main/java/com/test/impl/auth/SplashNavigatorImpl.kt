package com.test.impl.auth

import androidx.navigation.NavController
import com.test.navigation.auth.SplashNavigator
import com.test.splash.ui.SplashFragmentDirections

class SplashNavigatorImpl : SplashNavigator {

    override fun toMainActivity(
        navController: NavController,
        notificationType: String?,
        cocktailId: String?
    ) {
        val action = SplashFragmentDirections
            .actionSplashFragmentToMainActivity(notificationType, cocktailId)
        navController.navigate(action)
    }

    override fun toLoginFragment(navController: NavController) {
        val action = SplashFragmentDirections.actionSplashFragmentToLoginFragment()
        navController.navigate(action)
    }
}
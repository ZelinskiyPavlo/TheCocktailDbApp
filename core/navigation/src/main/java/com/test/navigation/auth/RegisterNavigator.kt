package com.test.navigation.auth

import androidx.navigation.NavController

interface RegisterNavigator {

    fun toMainActivity(navController: NavController, notificationType: String?, cocktailId: String?)

    fun toLoginFragment(navController: NavController)
}
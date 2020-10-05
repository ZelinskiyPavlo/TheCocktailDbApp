package com.test.navigation.auth

import androidx.navigation.NavController

interface LoginNavigator {

    fun toMainActivity(navController: NavController, notificationType: String?, cocktailId: String?)

    fun toRegisterFragment(navController: NavController)
}
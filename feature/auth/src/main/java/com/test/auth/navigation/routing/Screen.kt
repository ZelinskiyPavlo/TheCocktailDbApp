package com.test.auth.navigation.routing

import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.test.login.ui.LoginFragment
import com.test.register.ui.RegisterFragment

internal object Screen {

    fun login() = FragmentScreen("LOGIN") { LoginFragment.newInstance() }

    fun register() = FragmentScreen("REGISTER") { RegisterFragment.newInstance() }
}
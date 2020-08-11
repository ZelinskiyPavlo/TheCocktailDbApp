package com.test.thecocktaildb.presentation.ui.auth

import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.test.thecocktaildb.R
import com.test.thecocktaildb.databinding.ActivityAuthBinding
import com.test.thecocktaildb.presentation.ui.base.BaseActivity
import com.test.thecocktaildb.util.locale.setLocale

class AuthActivity : BaseActivity<ActivityAuthBinding, AuthViewModel>() {

    override val contentLayoutResId: Int = R.layout.activity_auth

    private lateinit var navController: NavController

    override val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        setLocale(this)
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)

        initNavController()
    }

    private fun initNavController() {
        navController = findNavController(R.id.auth_nav_host_fragment)
    }
}
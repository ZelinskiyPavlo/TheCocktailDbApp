package com.test.thecocktaildb.ui.auth

import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.test.thecocktaildb.R
import com.test.thecocktaildb.databinding.ActivityAuthBinding
import com.test.thecocktaildb.ui.base.BaseActivity

class AuthActivity : BaseActivity<ActivityAuthBinding, AuthViewModel>() {

    override val contentLayoutResId: Int = R.layout.activity_auth

    private lateinit var navController: NavController

    override val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)

        initNavController()
    }

    private fun initNavController() {
        navController = findNavController(R.id.auth_nav_host_fragment)
    }

    companion object {
        const val EXTRA_KEY_LOG_OUT_EVENT = "EXTRA_KEY_LOG_OUT_EVENT"
    }
}
package com.test.thecocktaildb.presentation.ui.auth

import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.test.thecocktaildb.R
import com.test.thecocktaildb.databinding.ActivityAuthBinding
import com.test.thecocktaildb.presentation.ui.base.BaseActivity
import com.test.thecocktaildb.util.locale.setLocale
import timber.log.Timber

class AuthActivity : BaseActivity<ActivityAuthBinding, AuthViewModel>() {

    override val contentLayoutResId: Int = R.layout.activity_auth

    private lateinit var navController: NavController

    override val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        setLocale(this)
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)

        initNavController()

        fetchDataFromRemoteConfig()
    }

    private fun initNavController() {
        navController = findNavController(R.id.auth_nav_host_fragment)
    }

    private fun fetchDataFromRemoteConfig() {
        with(Firebase.remoteConfig) {
            setConfigSettingsAsync(remoteConfigSettings {
                minimumFetchIntervalInSeconds = 3600
            })
        }

        firebaseRemoteConfig.fetchAndActivate().addOnCompleteListener {
            if (it.isSuccessful) {
                Timber.i("Remote config fetch successfully completed")
            } else {
                Timber.i("Remote config fetch completed with error")
            }
        }
    }
}
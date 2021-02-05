package com.test.splash.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.test.common.exception.NoInternetConnectionError
import com.test.presentation.factory.SavedStateViewModelFactory
import com.test.presentation.ui.base.BaseFragment
import com.test.presentation.util.EventObserver
import com.test.splash.R
import com.test.splash.databinding.FragmentSplashBinding
import com.test.splash.factory.SplashViewModelFactory
import com.test.splash.navigation.SplashNavigationApi
import javax.inject.Inject

class SplashFragment : BaseFragment<FragmentSplashBinding>() {

    companion object {
        @JvmStatic
        fun newInstance(): SplashFragment {
            return SplashFragment()
        }
    }

    override val layoutId: Int = R.layout.fragment_splash

    @Inject
    lateinit var splashViewModelFactory: SplashViewModelFactory

    override val viewModel: SplashViewModel by viewModels {
        SavedStateViewModelFactory(splashViewModelFactory, this)
    }

    @Inject
    lateinit var splashNavigator: SplashNavigationApi

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        setupObserver()
        startNavigationFlow()
        return viewDataBinding.root
    }

    override fun configureDataBinding() {
        super.configureDataBinding()
        viewDataBinding.fragment = this
    }

    private fun setupObserver() {
        viewModel.internetStatusEventLiveData.observe(
            viewLifecycleOwner, EventObserver { networkStatusResult ->
                onNetworkChecked(networkStatusResult)
            })
        viewModel.loginStatusEventLiveData.observe(
            viewLifecycleOwner, EventObserver { isUserLoggedIn ->
                navigateToDestination(isUserLoggedIn)
            })
    }

    private fun startNavigationFlow() {
        viewModel.checkInternetConnection()
    }

    fun onSplashClicked() {
        viewModel.checkInternetConnection()
    }

    private fun onNetworkChecked(isNetworkAvailable: Boolean) {
        if (isNetworkAvailable) viewModel.checkLoginStatus()
        // TODO: 31.01.2021 can i improve this login by using cicerone showSystemMessage
        else handleNoInternetConnectionError(NoInternetConnectionError())
    }

    private fun navigateToDestination(isUserLoggedIn: Boolean) {
        if (isUserLoggedIn) navigateToCocktailActivity()
        else navigateToLoginFragment()
    }

    private fun navigateToLoginFragment() {
        splashNavigator.toAuth()
    }

    private fun navigateToCocktailActivity() {
//        val (notificationType, cocktailId) = sharedViewModel.firebaseData

//        splashNavigator.toCocktail(notificationType, cocktailId)
    }
}
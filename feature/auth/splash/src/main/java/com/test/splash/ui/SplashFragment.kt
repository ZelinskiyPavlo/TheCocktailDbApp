package com.test.splash.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.test.auth.ui.AuthViewModel
import com.test.common.exception.NoInternetConnectionError
import com.test.navigation.auth.SplashNavigator
import com.test.presentation.factory.SavedStateViewModelFactory
import com.test.presentation.ui.base.BaseFragment
import com.test.presentation.util.EventObserver
import com.test.splash.R
import com.test.splash.factory.SplashViewModelFactory
import com.test.splash.databinding.FragmentSplashBinding
import dagger.android.AndroidInjection
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

    @Inject
    lateinit var splashNavigator: SplashNavigator

    override val viewModel: SplashViewModel by viewModels {
        SavedStateViewModelFactory(splashViewModelFactory, this)
    }

    private val sharedViewModel: AuthViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
//        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        setupObserver()
        navigateToNextScreen()
        return viewDataBinding.root
    }

    override fun configureDataBinding() {
        super.configureDataBinding()
        viewDataBinding.fragment = this
    }

    private fun setupObserver() {
        viewModel.internetStatusEventLiveData.observe(viewLifecycleOwner, EventObserver {
            if (it) navigateToNextScreen()
            else handleNoInternetConnectionError(NoInternetConnectionError())
        })
    }

    private fun navigateToNextScreen() {
        viewModel.loginStatusEventLiveData.observe(viewLifecycleOwner, EventObserver { isLoggedIn ->
            if (isLoggedIn) navigateToCocktailActivity()
            else navigateToLoginFragment()
        })
        viewModel.checkLoginStatus()
    }

    private fun navigateToLoginFragment() {
        splashNavigator.toLoginFragment(findNavController())
    }

    private fun navigateToCocktailActivity() {
        val (notificationType, cocktailId) = sharedViewModel.firebaseData

        splashNavigator.toMainActivity(findNavController(), notificationType, cocktailId)
        activity?.finish()
    }

    fun checkInternetConnection() {
        viewModel.checkInternetConnection()
    }
}
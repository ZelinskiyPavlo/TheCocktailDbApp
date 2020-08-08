package com.test.thecocktaildb.ui.auth.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.test.thecocktaildb.R
import com.test.thecocktaildb.databinding.FragmentSplashBinding
import com.test.thecocktaildb.di.Injectable
import com.test.thecocktaildb.ui.auth.AuthActivity
import com.test.thecocktaildb.ui.base.BaseFragment
import com.test.thecocktaildb.util.EventObserver
import com.test.thecocktaildb.util.SavedStateViewModelFactory
import com.test.thecocktaildb.util.SplashViewModelFactory
import javax.inject.Inject

class SplashFragment : BaseFragment<FragmentSplashBinding>(), Injectable {

    override val layoutId: Int = R.layout.fragment_splash

    @Inject
    lateinit var splashViewModelFactory: SplashViewModelFactory

    override val viewModel: SplashViewModel by viewModels {
        SavedStateViewModelFactory(splashViewModelFactory, this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        determineNextDestination()
        return viewDataBinding.root
    }

    private fun determineNextDestination() {
        if (!isLogOutEvent()) {
            navigateToNextScreen()
        }
    }

    private fun isLogOutEvent(): Boolean {
        requireActivity().intent?.extras?.getBoolean(AuthActivity.EXTRA_KEY_LOG_OUT_EVENT)
            ?.let { logOutEvent ->
                if (logOutEvent) {
                    navigateToLoginFragment()
                    return true
                }
            }
        return false
    }

    private fun navigateToNextScreen() {
        viewModel.loginStatusEventLiveData.observe(viewLifecycleOwner, EventObserver { isLoggedIn ->
            if (isLoggedIn) navigateToCocktailActivity()
            else navigateToLoginFragment()
        })
        viewModel.checkLoginStatus()

    }

    private fun navigateToLoginFragment() {
        val action = SplashFragmentDirections.actionSplashFragmentToLoginFragment()
        findNavController().navigate(action)
    }

    private fun navigateToCocktailActivity() {
        val action = SplashFragmentDirections.actionSplashFragmentToMainActivity()
        findNavController().navigate(action)
        activity?.finish()
    }
}
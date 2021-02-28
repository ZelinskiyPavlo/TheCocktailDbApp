package com.test.auth.ui

import android.os.Bundle
import com.github.terrakok.cicerone.Navigator
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import com.github.terrakok.cicerone.androidx.AppNavigator
import com.test.auth.R
import com.test.auth.databinding.FragmentAuthHostBinding
import com.test.auth.navigation.routing.Screen
import com.test.navigation.HasBackPressLogic
import com.test.presentation.ui.base.BaseFragment
import javax.inject.Inject

class AuthHostFragment : BaseFragment<FragmentAuthHostBinding>(), HasBackPressLogic {

    companion object {
        @JvmStatic
        fun newInstance(): AuthHostFragment {
            return AuthHostFragment()
        }
    }

    override val layoutId: Int = R.layout.fragment_auth_host

    @Inject
    lateinit var navigatorHolder: NavigatorHolder

    @Inject
    lateinit var router: Router

    private lateinit var navigator: Navigator

    private val currentFragment: BaseFragment<*>?
        get() = childFragmentManager.findFragmentById(R.id.auth_fragment_container) as? BaseFragment<*>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navigator =
            AppNavigator(requireActivity(), R.id.auth_fragment_container, childFragmentManager)
        router.replaceScreen(Screen.login())
    }

    override fun onResume() {
        super.onResume()
        navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        navigatorHolder.removeNavigator()
        super.onPause()
    }

    override fun onBackPressed() {
        if (currentFragment is HasBackPressLogic)
            currentFragment?.onBackPressed()
        else
            router.exit()
    }
}
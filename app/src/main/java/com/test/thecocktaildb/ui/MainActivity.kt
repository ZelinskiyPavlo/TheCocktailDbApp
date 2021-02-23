package com.test.thecocktaildb.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.github.terrakok.cicerone.Navigator
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import com.github.terrakok.cicerone.androidx.AppNavigator
import com.test.firebase.common.DynamicLink
import com.test.firebase.common.Fcm
import com.test.navigation.HasBackPressLogic
import com.test.presentation.extension.observeOnce
import com.test.presentation.factory.SavedStateViewModelFactory
import com.test.presentation.ui.base.BaseFragment
import com.test.presentation.util.EventObserver
import com.test.thecocktaildb.R
import com.test.thecocktaildb.databinding.ActivityMainBinding
import com.test.thecocktaildb.di.DiConstant
import com.test.thecocktaildb.factory.MainViewModelFactory
import com.test.thecocktaildb.navigation.deeplink.DeepLinkNavigationHandler
import com.test.thecocktaildb.navigation.routing.Screen
import com.test.thecocktaildb.navigation.state.NavigationStateHolder
import com.test.thecocktaildb.ui.base.BaseActivity
import dagger.android.AndroidInjection
import javax.inject.Inject
import javax.inject.Named

class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {

    override val contentLayoutResId: Int = R.layout.activity_main

    @Inject
    lateinit var viewModelFactory: MainViewModelFactory

    override val viewModel: MainViewModel by viewModels {
        SavedStateViewModelFactory(viewModelFactory, this)
    }

    @Inject
    @field:Named(DiConstant.GLOBAL)
    lateinit var navigatorHolder: NavigatorHolder

    @Inject
    @field:Named(DiConstant.GLOBAL)
    lateinit var router: Router

    @Inject
    lateinit var navigationStateHolder: NavigationStateHolder

    private lateinit var navigator: Navigator

    private val currentFragment: BaseFragment<*>?
        get() = supportFragmentManager.findFragmentById(R.id.main_fragment_container) as? BaseFragment<*>

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)

        viewModel.refreshUser()
        setupAppNavigator()
        setupObserver()
        viewModel.userLiveData.observeOnce {
            handleNavigation()
        }
    }

    private fun setupAppNavigator() {
        navigator = AppNavigator(this, R.id.main_fragment_container)
    }

    private fun setupObserver() {
        navigationStateHolder.deferredDeepLinkEvent.observe(this, EventObserver { model ->
            DeepLinkNavigationHandler(navigator, navigationStateHolder, viewModel.isUserLoggedIn)
                .performNavigation(model)
        })
    }

    private fun handleNavigation() {
        when {
            handleDynamicLink() -> {}
            viewModel.isUserLoggedIn -> navigateToTabHost()
            else -> navigateToAuth()
        }
    }

    private fun navigateToTabHost() {
        router.replaceScreen(Screen.tabHost())
    }

    private fun navigateToAuth() {
        router.replaceScreen(Screen.auth())
    }

    private fun handleDynamicLink(): Boolean {
        intent.data?.let { uri ->
            uri.getQueryParameter(DynamicLink.LINK) ?: return false

            DeepLinkNavigationHandler(navigator, navigationStateHolder, viewModel.isUserLoggedIn)
                .processDeepLink(uri)

            return true
        }
        return false
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        handleNotification(intent)
    }

    private fun handleNotification(newIntent: Intent?) {
        newIntent?.extras?.getString(Fcm.EXTRA_KEY_NOTIFICATION_TYPE)?.also { notificationType ->
            DeepLinkNavigationHandler(navigator, navigationStateHolder, viewModel.isUserLoggedIn)
                .processDeepLink(
                    notificationType,
                    newIntent.extras?.getString(Fcm.EXTRA_KEY_COCKTAIL_ID)?.toLong()
                )
        }
    }

    override fun onBackPressed() {
        if (currentFragment is HasBackPressLogic)
            currentFragment?.onBackPressed()
        else
            router.exit()
    }

    override fun onResume() {
        super.onResume()
        navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        navigatorHolder.removeNavigator()
        super.onPause()
    }
}
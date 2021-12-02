package com.test.thecocktaildb.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.github.terrakok.cicerone.Navigator
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import com.github.terrakok.cicerone.androidx.AppNavigator
import com.test.firebase.common.DynamicLink
import com.test.firebase.common.Fcm
import com.test.navigation.HasBackPressLogic
import com.test.presentation.factory.SavedStateViewModelFactory
import com.test.presentation.ui.base.BaseFragment
import com.test.thecocktaildb.R
import com.test.thecocktaildb.databinding.ActivityMainBinding
import com.test.thecocktaildb.di.DiConstant
import com.test.thecocktaildb.factory.MainViewModelFactory
import com.test.thecocktaildb.navigation.deeplink.DeepLinkNavigationHandler
import com.test.thecocktaildb.navigation.routing.Screen
import com.test.thecocktaildb.navigation.state.NavigationStateHolder
import com.test.thecocktaildb.ui.base.BaseActivity
import dagger.android.AndroidInjection
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
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
    @Named(DiConstant.GLOBAL)
    lateinit var navigatorHolder: NavigatorHolder

    @Inject
    @Named(DiConstant.GLOBAL)
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

        configureNavigation(savedInstanceState)
    }

    override fun setupObservers() {
        super.setupObservers()
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.eventsFlow.onEach { event ->
                    when(event) {
                        is MainViewModel.Event.InitNavigation -> handleNavigation()
                    }
                }.launchIn(this)

                navigationStateHolder.deferredDeepLinkFlow.onEach { model ->
                    DeepLinkNavigationHandler(navigator, navigationStateHolder, viewModel.isUserLoggedInFlow.value)
                        .performNavigation(model)
                }.launchIn(this)
            }
        }
    }

    private fun configureNavigation(savedInstanceState: Bundle?) {
        navigator = AppNavigator(this, R.id.main_fragment_container)
        if(savedInstanceState == null) {
            viewModel.handleColdStart()
        }
    }

    private fun handleNavigation() {
        when {
            handleDynamicLink() -> {}
            viewModel.isUserLoggedInFlow.value -> navigateToTabHost()
            else -> navigateToAuth()
        }
    }

    private fun navigateToTabHost() {
        router.replaceScreen(Screen.tabHost())
    }

    private fun navigateToAuth() {
        router.replaceScreen(Screen.auth())
    }

    private fun handleDynamicLink(newIntent: Intent? = null): Boolean {
        val intentData = newIntent?.data ?: intent.data
        intentData?.let { uri ->
            uri.getQueryParameter(DynamicLink.LINK) ?: return false

            return DeepLinkNavigationHandler(
                navigator,
                navigationStateHolder,
                viewModel.isUserLoggedInFlow.value
            ).processDeepLink(uri)
        }
        return false
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        handleNotification(intent)
        handleDynamicLink(intent)
    }

    private fun handleNotification(newIntent: Intent?) {
        newIntent?.extras?.getString(Fcm.EXTRA_KEY_NOTIFICATION_TYPE)?.also { notificationType ->
            DeepLinkNavigationHandler(navigator, navigationStateHolder, viewModel.isUserLoggedInFlow.value)
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
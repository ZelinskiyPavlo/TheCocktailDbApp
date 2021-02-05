package com.test.thecocktaildb.ui

import android.os.Bundle
import androidx.activity.viewModels
import com.github.terrakok.cicerone.Navigator
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import com.github.terrakok.cicerone.androidx.AppNavigator
import com.test.presentation.ui.base.BaseFragment
import com.test.thecocktaildb.R
import com.test.thecocktaildb.databinding.ActivityMainBinding
import com.test.thecocktaildb.di.DiConstant
import com.test.thecocktaildb.navigation.routing.Screen
import com.test.thecocktaildb.ui.base.BaseActivity
import dagger.android.AndroidInjection
import javax.inject.Inject
import javax.inject.Named

class MainActivity: BaseActivity<ActivityMainBinding, MainViewModel>() {

    override val contentLayoutResId: Int = R.layout.activity_main

    override val viewModel: MainViewModel by viewModels()

    @Inject
    @field:Named(DiConstant.GLOBAL)
    lateinit var navigatorHolder: NavigatorHolder

    @Inject
    @field:Named(DiConstant.GLOBAL)
    lateinit var router: Router

    lateinit var navigator: Navigator

    private val currentFragment: BaseFragment<*>?
        get() = supportFragmentManager.findFragmentById(R.id.main_fragment_container) as? BaseFragment<*>

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)

        navigator = AppNavigator(this, R.id.main_fragment_container)
        router.replaceScreen(Screen.splash())
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
        currentFragment?.onBackPressed() ?: super.onBackPressed()
    }
}
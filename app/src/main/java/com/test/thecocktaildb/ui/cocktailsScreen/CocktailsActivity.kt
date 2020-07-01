package com.test.thecocktaildb.ui.cocktailsScreen

import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.LabelVisibilityMode
import com.test.thecocktaildb.R
import com.test.thecocktaildb.data.Cocktail
import com.test.thecocktaildb.ui.base.BaseActivity
import com.test.thecocktaildb.ui.cocktailsScreen.callback.FragmentEventCallback
import com.test.thecocktaildb.ui.cocktailsScreen.callback.OnFavoriteClicked
import com.test.thecocktaildb.ui.cocktailsScreen.callback.OnFilterApplied
import com.test.thecocktaildb.ui.cocktailsScreen.drinkFilter.DrinkFilter
import com.test.thecocktaildb.ui.cocktailsScreen.fragmentHostScreen.HostFragment
import com.test.thecocktaildb.ui.cocktailsScreen.sortType.CocktailSortType
import com.test.thecocktaildb.ui.profileScreen.ProfileFragment
import com.test.thecocktaildb.util.receiver.AirplaneReceiver
import kotlinx.android.synthetic.main.activity_main.*

class CocktailsActivity @Inject constructor() : BaseActivity(),
    FragmentEventCallback, LifecycleObserver, OnFavoriteClicked, HasAndroidInjector,
    BaseDialogFragment.OnDialogFragmentClickListener<Any, DialogButton, DialogType<DialogButton>>{

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    @Inject
    lateinit var viewModelFactory: DelegatedViewModelFactory

    private val mainViewModel: MainViewModel by viewModels{viewModelFactory}

    private val sharedMainViewModel: SharedMainViewModel by viewModels()

    private lateinit var airplaneBroadcastReceiver: BroadcastReceiver

    private var navHost: Fragment? = null

    private var listeners: MutableSet<OnFilterApplied> = mutableSetOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initNavHost()

        setupBottomNavigation()
    }

    private fun initNavHost() {
        navHost = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
    }

    private fun setupBottomNavigation() {
        val bottomNavigation = main_activity_bnv
        bottomNavigation.selectedItemId = R.id.bnv_main_fragment

        fun changeBottomNavTitleVisibility(isVisible: Boolean) {
            bottomNavigation.labelVisibilityMode =
                if (isVisible) LabelVisibilityMode.LABEL_VISIBILITY_LABELED
                else LabelVisibilityMode.LABEL_VISIBILITY_UNLABELED
        }

        profileFragment = ProfileFragment.newInstance()
        supportFragmentManager.beginTransaction()
            .replace(R.id.profile_fragment_container, profileFragment)
            .hide(profileFragment)
            .commit()

        bottomNavigation.setOnNavigationItemSelectedListener { item: MenuItem ->
            when(item.itemId) {
                R.id.bnv_main_fragment -> {
                    supportFragmentManager.beginTransaction()
                        .hide(profileFragment)
                        .show(navHost!!)
                        .setPrimaryNavigationFragment(navHost!!)
                        .commit()
                    true
                }
                R.id.bnv_profile_fragment -> {
                    if (navHost != null) {
                        supportFragmentManager.beginTransaction()
                            .show(profileFragment)
                            .hide(navHost!!)
                            .setPrimaryNavigationFragment(profileFragment)
                            .commit()
                    }
                    true
                }
                else -> false
            }
        }
    }

    override fun onStart() {
        super.onStart()

        airplaneBroadcastReceiver =
            AirplaneReceiver()
        val intentFilter = IntentFilter().apply {
            addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED)
        }
        registerReceiver(airplaneBroadcastReceiver, intentFilter)
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(airplaneBroadcastReceiver)
    }

    override fun navigateToHostFragmentEvent(filterTypeList: List<DrinkFilter?>) {
        listeners.forEach { it.applyFilter(filterTypeList) }

        supportFragmentManager.popBackStack()
    }

    override fun resetFilterEvent() {
        listeners.forEach { it.resetFilter() }
    }

    override fun applySortingEvent(cocktailSortType: CocktailSortType?) {
        listeners.forEach { it.applySorting(cocktailSortType) }
    }

    override fun addCallback(listener: OnFilterApplied) {
        listeners.add(listener)
    }

    override fun removeCallback(listener: OnFilterApplied) {
        listeners.remove(listener)
    }

    override fun onFavoriteAdded(cocktail: Cocktail) {
        navHost?.let { navFragment ->
            navFragment.childFragmentManager.primaryNavigationFragment?.let { hostFragment ->
                hostFragment as HostFragment
                hostFragment.onFavoriteAdded(cocktail)
            }
        }
    }
}

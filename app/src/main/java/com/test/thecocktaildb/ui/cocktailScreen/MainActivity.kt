package com.test.thecocktaildb.ui.cocktailScreen

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
import com.test.thecocktaildb.databinding.ActivityMainBinding
import com.test.thecocktaildb.ui.base.BaseActivity
import com.test.thecocktaildb.ui.base.BaseDialogFragment
import com.test.thecocktaildb.ui.cocktailScreen.fragmentHostScreen.HostFragmentDirections
import com.test.thecocktaildb.ui.dialog.*
import com.test.thecocktaildb.ui.profileScreen.ProfileFragment
import com.test.thecocktaildb.util.EventObserver
import com.test.thecocktaildb.util.GenericSavedStateViewModelFactory
import com.test.thecocktaildb.util.MainViewModelFactory
import com.test.thecocktaildb.util.receiver.AirplaneReceiver
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

var lastSavedTime: Long? = null

class MainActivity : BaseActivity<ActivityMainBinding>(), LifecycleObserver,
    BaseDialogFragment.OnDialogFragmentClickListener<Any, DialogButton, DialogType<DialogButton>> {

    override val contentLayoutResId: Int = R.layout.activity_main

    @Inject
    lateinit var mainViewModelFactory: MainViewModelFactory

    private val mainViewModel: MainViewModel by viewModels {
        GenericSavedStateViewModelFactory(mainViewModelFactory, this) }

    private val sharedMainViewModel: SharedMainViewModel by viewModels()

    private lateinit var airplaneBroadcastReceiver: BroadcastReceiver

    private var navHost: Fragment? = null

    private lateinit var profileFragment: ProfileFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        initNavHost()
        attachLifecycleObserver()

        setupNavigation()
        setupBottomNavigation()
    }

    private fun initNavHost() {
        navHost = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
    }

    private fun attachLifecycleObserver() {
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    private fun setupNavigation() {
        mainViewModel.cocktailDetailsEventLiveData.observe(this,
            EventObserver {
                val (actionBarTitle, cocktailId) = it
                val action = HostFragmentDirections
                    .actionHostFragmentToCocktailDetailsFragment(actionBarTitle, cocktailId)
                navHost?.findNavController()?.navigate(action)
            })
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
            when (item.itemId) {
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

        sharedMainViewModel.isCheckBoxCheckedLiveData.observe(this, Observer {
            changeBottomNavTitleVisibility(it)
        })
    }

    override fun onStart() {
        super.onStart()

        airplaneBroadcastReceiver = AirplaneReceiver()
        val intentFilter = IntentFilter().apply {
            addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED)
        }
        registerReceiver(airplaneBroadcastReceiver, intentFilter)
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(airplaneBroadcastReceiver)
    }

    @Suppress("unused")
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun checkLastClosedTime() {
        lastSavedTime?.let {
            val currentTime = System.currentTimeMillis()
            if (currentTime - it > 10000) showCocktailOfTheDayDialog()
        }
    }

    override fun onDestroy() {
        ProcessLifecycleOwner.get().lifecycle.removeObserver(this)
        super.onDestroy()
    }

    @Suppress("unused")
    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun saveClosedTime() {
        lastSavedTime = System.currentTimeMillis()
    }

    // TODO: extract string resources
    private fun showCocktailOfTheDayDialog() {
        RegularDialogFragment.newInstance {
            titleText = "Cocktail of the day"
            descriptionText = "Do you want to see cocktail of the day"
            leftButtonText = "No"
            rightButtonText = "Yes"
        }.show(supportFragmentManager, "CocktailOfTheDayDialog")
    }

    override fun onDialogFragmentClick(
        dialog: DialogFragment,
        dialogType: DialogType<DialogButton>,
        buttonType: DialogButton,
        data: Any?
    ) {
        super.onDialogFragmentClick(dialog, dialogType, buttonType, data)

        when (dialogType) {
            RegularDialogType -> {
                when (buttonType) {
                    LeftDialogButton -> dialog.dismiss()
                    RightDialogButton -> {
                        mainViewModel.openCocktail()
                    }
                }
            }
        }
    }
}

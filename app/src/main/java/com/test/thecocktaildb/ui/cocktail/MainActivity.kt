package com.test.thecocktaildb.ui.cocktail

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
import com.test.thecocktaildb.ui.cocktail.host.HostFragmentDirections
import com.test.thecocktaildb.ui.dialog.*
import com.test.thecocktaildb.ui.dialog.base.BaseDialogFragment
import com.test.thecocktaildb.ui.profile.ProfileFragment
import com.test.thecocktaildb.util.EventObserver
import com.test.thecocktaildb.util.MainViewModelFactory
import com.test.thecocktaildb.util.SavedStateViewModelFactory
import com.test.thecocktaildb.util.receiver.AirplaneReceiver
import com.test.thecocktaildb.util.setLocale
import icepick.State
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

var lastSavedTime: Long? = null

class MainActivity : BaseActivity<ActivityMainBinding>(), LifecycleObserver,
    BaseDialogFragment.OnDialogFragmentClickListener<Any, DialogButton, DialogType<DialogButton>> {

    override val contentLayoutResId: Int = R.layout.activity_main

    @Inject
    lateinit var mainViewModelFactory: MainViewModelFactory

    private val mainViewModel: MainViewModel by viewModels {
        SavedStateViewModelFactory(mainViewModelFactory, this)
    }

    private val sharedMainViewModel: SharedMainViewModel by viewModels()

    private lateinit var airplaneBroadcastReceiver: BroadcastReceiver

    private var navHost: Fragment? = null

    private lateinit var profileFragment: ProfileFragment

    @JvmField
    @State
    var selectedTab: Int? = null

    private var cocktailOfTheDayDialog: RegularDialogFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setLocale(this)
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
                    selectedTab = R.id.bnv_main_fragment
                    supportFragmentManager.beginTransaction()
                        .hide(profileFragment)
                        .show(navHost!!)
                        .setPrimaryNavigationFragment(navHost!!)
                        .commit()
                    true
                }
                R.id.bnv_profile_fragment -> {
                    if (navHost != null) {
                        selectedTab = R.id.bnv_profile_fragment
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

        bottomNavigation.selectedItemId = selectedTab ?: R.id.bnv_main_fragment
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
            if (currentTime - it > 10000) {
                showCocktailOfTheDayDialog()
                lastSavedTime = null
            }
        }
    }

    @Suppress("unused")
    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun saveClosedTime() {
        lastSavedTime = System.currentTimeMillis()
    }

    override fun onDestroy() {
        ProcessLifecycleOwner.get().lifecycle.removeObserver(this)
        super.onDestroy()
    }

    private fun showCocktailOfTheDayDialog() {
        cocktailOfTheDayDialog =
            supportFragmentManager.findFragmentByTag("CocktailOfTheDayDialog") as? RegularDialogFragment
        if (cocktailOfTheDayDialog == null) {
            cocktailOfTheDayDialog = RegularDialogFragment.newInstance {
                titleText = getString(R.string.dialog_cocktail_of_the_day_title)
                descriptionText = getString(R.string.dialog_cocktail_of_the_day_description)
                leftButtonText = getString(R.string.all_no)
                rightButtonText = getString(R.string.all_yes)
            }
        }
        if (!cocktailOfTheDayDialog!!.isVisible && !cocktailOfTheDayDialog!!.isAdded)
            cocktailOfTheDayDialog!!.show(supportFragmentManager, "CocktailOfTheDayDialog")
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

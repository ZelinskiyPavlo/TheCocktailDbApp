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
import com.test.thecocktaildb.ui.setting.SettingFragment
import com.test.thecocktaildb.util.EventObserver
import com.test.thecocktaildb.util.MainViewModelFactory
import com.test.thecocktaildb.util.SavedStateViewModelFactory
import com.test.thecocktaildb.util.receiver.AirplaneReceiver
import icepick.State
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

var lastSavedTime: Long? = null

class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>(), LifecycleObserver,
    BaseDialogFragment.OnDialogFragmentClickListener<Any, DialogButton, DialogType<DialogButton>> {

    override val contentLayoutResId: Int = R.layout.activity_main

    @Inject
    lateinit var mainViewModelFactory: MainViewModelFactory

    override val viewModel: MainViewModel by viewModels {
        SavedStateViewModelFactory(mainViewModelFactory, this)
    }

    private val sharedMainViewModel: SharedMainViewModel by viewModels()

    private lateinit var airplaneBroadcastReceiver: BroadcastReceiver

    private var navHost: Fragment? = null

    private lateinit var settingFragment: SettingFragment

    @JvmField
    @State
    var selectedTab: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        initNavHost()
        attachLifecycleObserver()

        setupNavigation()
        setupBottomNavigation(savedInstanceState)
    }

    private fun initNavHost() {
        navHost = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
    }

    private fun attachLifecycleObserver() {
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    private fun setupNavigation() {
        viewModel.cocktailDetailsEventLiveData.observe(this,
            EventObserver {
                val (actionBarTitle, cocktailId) = it
                val action = HostFragmentDirections
                    .actionHostFragmentToCocktailDetailsFragment(actionBarTitle, cocktailId)
                navHost?.findNavController()?.navigate(action)
            })
    }

    private fun setupBottomNavigation(savedInstanceState: Bundle?) {
        val bottomNavigation = main_activity_bnv

        fun changeBottomNavTitleVisibility(isVisible: Boolean) {
            bottomNavigation.labelVisibilityMode =
                if (isVisible) LabelVisibilityMode.LABEL_VISIBILITY_LABELED
                else LabelVisibilityMode.LABEL_VISIBILITY_UNLABELED
        }

        createOrRestoreSettingFragment(savedInstanceState)

        bottomNavigation.setOnNavigationItemSelectedListener { item: MenuItem ->
            when (item.itemId) {
                R.id.bnv_main_fragment -> {
                    selectedTab = R.id.bnv_main_fragment
                    supportFragmentManager.beginTransaction()
                        .hide(settingFragment)
                        .show(navHost!!)
                        .setPrimaryNavigationFragment(navHost!!)
                        .commit()
                    true
                }
                R.id.bnv_setting_fragment -> {
                    if (navHost != null) {
                        selectedTab = R.id.bnv_setting_fragment
                        supportFragmentManager.beginTransaction()
                            .show(settingFragment)
                            .hide(navHost!!)
                            .setPrimaryNavigationFragment(settingFragment)
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

    private fun createOrRestoreSettingFragment(savedInstanceState: Bundle?) {
        settingFragment = SettingFragment.newInstance()
        if (savedInstanceState != null) {
            settingFragment.setInitialSavedState(
                savedInstanceState.getParcelable(SettingFragment::class.java.name)
            )
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.setting_fragment_container, settingFragment)
            .hide(settingFragment)
            .commit()
    }


    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelable(
            SettingFragment::class.java.name,
            supportFragmentManager.saveFragmentInstanceState(settingFragment)
        )
        super.onSaveInstanceState(outState)
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
            if (currentTime - it > 10000 && checkSuitableFragment()) showCocktailOfTheDayDialog()
        }
    }

    override fun onDestroy() {
        ProcessLifecycleOwner.get().lifecycle.removeObserver(this)
        lastSavedTime = null
        super.onDestroy()
    }

    @Suppress("unused")
    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun saveClosedTime() {
        lastSavedTime = System.currentTimeMillis()
    }

    private fun checkSuitableFragment(): Boolean {
        return supportFragmentManager.fragments.lastOrNull().let { fragment ->
            return@let fragment != null && fragment.id != R.id.setting_fragment_container
        }
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
                        viewModel.openCocktail()
                    }
                }
            }
        }
    }
}

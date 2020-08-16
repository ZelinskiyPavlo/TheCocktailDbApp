package com.test.thecocktaildb.presentation.ui.cocktail

import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.core.os.bundleOf
import androidx.core.text.isDigitsOnly
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.navigation.fragment.findNavController
import androidx.navigation.navArgs
import com.google.android.material.bottomnavigation.LabelVisibilityMode
import com.test.thecocktaildb.R
import com.test.thecocktaildb.core.common.firebase.Analytic
import com.test.thecocktaildb.core.common.firebase.DynamicLink
import com.test.thecocktaildb.core.common.firebase.Fcm
import com.test.thecocktaildb.databinding.ActivityMainBinding
import com.test.thecocktaildb.presentation.callback.NotificationCallback
import com.test.thecocktaildb.presentation.ui.base.BaseActivity
import com.test.thecocktaildb.presentation.ui.cocktail.host.HostFragmentDirections
import com.test.thecocktaildb.presentation.ui.dialog.*
import com.test.thecocktaildb.presentation.ui.dialog.base.BaseDialogFragment
import com.test.thecocktaildb.presentation.ui.setting.SettingFragment
import com.test.thecocktaildb.util.EventObserver
import com.test.thecocktaildb.util.MainViewModelFactory
import com.test.thecocktaildb.util.SavedStateViewModelFactory
import com.test.thecocktaildb.util.locale.setLocale
import com.test.thecocktaildb.util.receiver.AirplaneReceiver
import com.test.thecocktaildb.util.receiver.NotificationReceiver
import com.test.thecocktaildb.util.service.NotificationType
import icepick.State
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

var lastSavedTime: Long? = null

class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>(), LifecycleObserver,
    NotificationCallback,
    BaseDialogFragment.OnDialogFragmentClickListener<Any, DialogButton, DialogType<DialogButton>> {

    override val contentLayoutResId: Int = R.layout.activity_main

    @Inject
    lateinit var mainViewModelFactory: MainViewModelFactory

    override val viewModel: MainViewModel by viewModels {
        SavedStateViewModelFactory(mainViewModelFactory, this)
    }

    private val sharedMainViewModel: SharedMainViewModel by viewModels()

    private lateinit var airplaneBroadcastReceiver: BroadcastReceiver
    private lateinit var notificationReceiver: BroadcastReceiver

    private var navHost: Fragment? = null

    private lateinit var settingFragment: SettingFragment

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
        setupBottomNavigation(savedInstanceState)

        handleFirebaseEvent()
    }

    private fun handleFirebaseEvent() {
        handleFcmNotification()
        handleDynamicLink()
        getDataFromRemoteConfig()
    }

    override fun configureObserver() {
        super.configureObserver()
        viewModel.showNoCocktailFoundDialogEventLiveData.observe(this, EventObserver {
            showNoCocktailFoundDialog(it)
        })
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

                    firebaseAnalytics.logEvent(
                        Analytic.MAIN_TAB_CHANGE,
                        bundleOf(Analytic.MAIN_TAB_CHANGE_KEY to "main")
                    )
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

                        firebaseAnalytics.logEvent(
                            Analytic.MAIN_TAB_CHANGE,
                            bundleOf(Analytic.MAIN_TAB_CHANGE_KEY to "profile")
                        )
                    }
                    true
                }
                else -> false
            }
        }

        sharedMainViewModel.shouldShowMainNavigationTitlesLiveData.observe(this, Observer {
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

    private fun handleFcmNotification(broadcastReceiverIntent: Intent? = null) {
        fun openMainFragment() {
            main_activity_bnv.selectedItemId = R.id.bnv_main_fragment
        }

        fun openProfileFragment() {
            main_activity_bnv.selectedItemId = R.id.bnv_setting_fragment
        }

        fun openProfileEditFragment() {
            openProfileFragment()
            settingFragment.openProfileFragment()
        }

        fun openCocktailDetailFragment(cocktailId: String) {
            viewModel.openCocktailById(cocktailId.toLong())
        }

        val args by navArgs<MainActivityArgs>()
        val notificationType =
            broadcastReceiverIntent?.extras?.getString(Fcm.EXTRA_KEY_NOTIFICATION_TYPE)
                ?: args.notificationType

        notificationType?.let {
            if (it.isDigitsOnly().not()) return
            when (NotificationType.values()[it.toInt()]) {
                NotificationType.MAIN -> openMainFragment()
                NotificationType.PROFILE -> openProfileFragment()
                NotificationType.PROFILE_EDIT -> openProfileEditFragment()
                NotificationType.DETAIL -> {
                    val cocktailId =
                        broadcastReceiverIntent?.extras?.getString(Fcm.EXTRA_KEY_COCKTAIL_ID)
                            ?: args.cocktailId!!
                    openCocktailDetailFragment(cocktailId)
                }
                NotificationType.RATE -> showRateDialog()
            }
        }
    }

    private fun handleDynamicLink() {
        fun openMainFragment() {
            main_activity_bnv.selectedItemId = R.id.bnv_main_fragment
        }

        fun openProfileEditFragment() {
            main_activity_bnv.selectedItemId = R.id.bnv_setting_fragment
            settingFragment.openProfileFragment()
        }

        fun openCocktailDetailFragment(cocktailId: String) {
            viewModel.openCocktailById(cocktailId.toLong())
        }

        val args by navArgs<MainActivityArgs>()
        val notificationType = args.notificationType

        notificationType?.let {
            when (it) {
                DynamicLink.TYPE_MAIN -> openMainFragment()
                DynamicLink.TYPE_PROFILE_EDIT -> openProfileEditFragment()
                DynamicLink.TYPE_COCKTAIL_DETAIL -> {
                    val cocktailId = args.cocktailId!!
                    openCocktailDetailFragment(cocktailId)
                }
            }
        }
    }

    private fun showRateDialog() {
        // TODO: 14.08.2020 create rate app dialog (using extra content feature)
    }

    private fun getDataFromRemoteConfig() {
        sharedMainViewModel.getDataFromRemoteConfig()
    }

    override fun onStart() {
        super.onStart()

        fun registerAirplaneReceiver() {
            airplaneBroadcastReceiver = AirplaneReceiver()
            val intentFilter = IntentFilter().apply {
                addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED)
            }
            registerReceiver(airplaneBroadcastReceiver, intentFilter)
        }

        fun registerNotificationReceiver() {
            notificationReceiver = NotificationReceiver(this)
            val intentFilter = IntentFilter().apply {
                addAction(Fcm.ACTION_NEW_NOTIFICATION)
            }
            registerReceiver(notificationReceiver, intentFilter)
        }

        registerAirplaneReceiver()
        registerNotificationReceiver()
    }

    override fun onStop() {
        super.onStop()

        unregisterReceiver(airplaneBroadcastReceiver)
        unregisterReceiver(notificationReceiver)
    }

    override fun handleBroadcastNotification(intent: Intent) {
        handleFcmNotification(intent)
    }

    @Suppress("unused")
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun checkLastClosedTime() {
        lastSavedTime?.let {
            val currentTime = System.currentTimeMillis()
            if (currentTime - it > 10000 && checkSuitableFragment()) {
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
        lastSavedTime = null
        super.onDestroy()
    }

    private fun checkSuitableFragment(): Boolean {
        return supportFragmentManager.findFragmentById(R.id.setting_fragment_container)
            ?.isHidden ?: false
    }

    private fun showCocktailOfTheDayDialog() {
        cocktailOfTheDayDialog =
            supportFragmentManager.findFragmentByTag("CocktailOfTheDayDialog") as? RegularDialogFragment
        if (cocktailOfTheDayDialog == null) {
            cocktailOfTheDayDialog = RegularDialogFragment.newInstance {
                titleTextResId = R.string.dialog_cocktail_of_the_day_title
                descriptionTextResId = R.string.dialog_cocktail_of_the_day_description
                leftButtonTextResId = R.string.all_no
                rightButtonTextResId = R.string.all_yes
            }
        }
        if (!cocktailOfTheDayDialog!!.isVisible)
            cocktailOfTheDayDialog!!.show(supportFragmentManager, "CocktailOfTheDayDialog")
    }

    private fun showNoCocktailFoundDialog(cocktailId: String) {
        RegularDialogFragment.newInstance {
            titleTextResId = R.string.dialog_no_cocktail_found_title
            descriptionText =
                "${getString(R.string.dialog_no_cocktail_found_description)} $cocktailId"
            rightButtonTextResId = R.string.all_ok
        }.show(supportFragmentManager, "NoCocktailFoundDialog")
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
                when (dialog.tag) {
                    "CocktailOfTheDayDialog" -> {
                        when (buttonType) {
                            LeftDialogButton -> dialog.dismiss()
                            RightDialogButton -> {
                                viewModel.openCocktailOfTheDay()
                            }
                        }
                    }
                    else -> {
                    }
                }
            }
        }
    }
}

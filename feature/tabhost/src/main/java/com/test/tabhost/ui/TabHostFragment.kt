package com.test.tabhost.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.github.terrakok.cicerone.Navigator
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import com.github.terrakok.cicerone.androidx.AppNavigator
import com.test.cocktail.ui.CommunicationViewModel
import com.test.navigation.HasBackPressLogic
import com.test.presentation.ui.base.BaseFragment
import com.test.tabhost.R
import com.test.tabhost.databinding.FragmentTabHostBinding
import com.test.tabhost.navigation.routing.Screen
import icepick.State
import javax.inject.Inject

class TabHostFragment : BaseFragment<FragmentTabHostBinding>(), HasBackPressLogic {

    override val layoutId: Int = R.layout.fragment_tab_host

    @JvmField
    @State
    var selectedTab: Int? = null

    private val cocktailCommunicationVM: CommunicationViewModel by viewModels()

    @Inject
    lateinit var navigatorHolder: NavigatorHolder

    @Inject
    lateinit var router: Router

    lateinit var navigator: Navigator

    @Inject
    lateinit var firebaseAnalytics: FirebaseAnalytics

    private val currentFragment: BaseFragment<*>?
        get() = childFragmentManager.findFragmentById(R.id.tab_host_container) as? BaseFragment<*>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        configureNavigation()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        setupBottomNavigation()
        setupObserver()

        return viewDataBinding.root
    }

    private fun configureNavigation() {
        navigator = AppNavigator(requireActivity(), R.id.tab_host_container, childFragmentManager)
    }

    private fun setupBottomNavigation() {
        val bottomNavigation = viewDataBinding.tabHostBottomNavigation

        bottomNavigation.setOnNavigationItemSelectedListener { item: MenuItem ->
            selectedTab = item.itemId
            when (item.itemId) {
                R.id.bnv_cocktail_item -> {
                    selectTab(Screen.Keys.COCKTAIL)

                    firebaseAnalytics.logCocktailTabClicked()
                    true
                }
                R.id.bnv_setting_item -> {
                    selectTab(Screen.Keys.SETTING)

                    firebaseAnalytics.logSettingTabClicked()
                    true
                }
                else -> false
            }
        }
        // TODO: 07.02.2021 прологувати поведінку і додати onReseletedListen при потребі

        bottomNavigation.selectedItemId = selectedTab ?: R.id.bnv_cocktail_item
    }

    //region Firebase Remote config

// Extracted from setupBottomNavigation()
//        sharedMainViewModel.shouldShowMainNavigationTitlesLiveData.observe(this, Observer {
//            changeBottomNavTitleVisibility(it)
//        })

//        fun changeBottomNavTitleVisibility(isVisible: Boolean) {
//            bottomNavigation.labelVisibilityMode =
//                if (isVisible) LabelVisibilityMode.LABEL_VISIBILITY_LABELED
//                else LabelVisibilityMode.LABEL_VISIBILITY_UNLABELED
//        }
    //endregion

    private fun selectTab(key: Screen.Keys) {
        val fragmentManager = childFragmentManager
        var currentFragment: Fragment? = null
        // Note, if I use lambda forEach, smart cast did not happened
        for (fragment in fragmentManager.fragments) {
            if (fragment.isVisible) {
                currentFragment = fragment
                break
            }
        }
        val newFragment = fragmentManager.findFragmentByTag(key.name)
        if (currentFragment != null && newFragment != null && currentFragment === newFragment) return
        val transaction = fragmentManager.beginTransaction()
        if (newFragment == null) {
            transaction.add(
                R.id.tab_host_container,
                key.getScreen().createFragment(fragmentManager.fragmentFactory),
                key.name
            )
        }
        if (currentFragment != null) {
            transaction.hide(currentFragment)
        }
        if (newFragment != null) {
            transaction.show(newFragment)
        }
        transaction.commitNow()
    }

    private fun setupObserver() {
        cocktailCommunicationVM.onNestedFragmentNavigationLiveData.observe(
            viewLifecycleOwner,
            { isDeeplyNestedFragmentShown ->
                viewDataBinding.tabHostBottomNavigation.visibility =
                    if (isDeeplyNestedFragmentShown) View.GONE
                    else View.VISIBLE
            })
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
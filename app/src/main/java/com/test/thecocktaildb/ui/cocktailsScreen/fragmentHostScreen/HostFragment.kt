package com.test.thecocktaildb.ui.cocktailsScreen.fragmentHostScreen

import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.*
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayoutMediator
import com.test.thecocktaildb.R
import com.test.thecocktaildb.databinding.FragmentHostBinding
import com.test.thecocktaildb.di.Injectable
import com.test.thecocktaildb.ui.base.BaseFragment
import com.test.thecocktaildb.ui.cocktailsScreen.CocktailPagerAdapter
import com.test.thecocktaildb.ui.cocktailsScreen.CocktailsFragment
import com.test.thecocktaildb.ui.cocktailsScreen.callback.BatteryStateCallback
import com.test.thecocktaildb.ui.cocktailsScreen.favoriteScreen.FavoriteFragment
import com.test.thecocktaildb.ui.cocktailsScreen.filterScreen.CocktailFilterFragment
import com.test.thecocktaildb.ui.cocktailsScreen.sortType.CocktailSortType
import com.test.thecocktaildb.util.BatteryStateHolder
import com.test.thecocktaildb.util.EventObserver
import com.test.thecocktaildb.util.receiver.BatteryStateReceiver

class HostFragment : BaseFragment<FragmentHostBinding, HostViewModel>(), Injectable,
    BatteryStateCallback {

    companion object {
        @JvmStatic
        fun newInstance(): HostFragment {
            return HostFragment()
        }
    }

    override val layoutId: Int = R.layout.fragment_host

    override fun getViewModelClass(): Class<HostViewModel> = HostViewModel::class.java

    private val sharedHostViewModel: SharedHostViewModel by activityViewModels { delegatedViewModelFactory }

    lateinit var viewPager: ViewPager2
    private lateinit var fragmentList: ArrayList<Fragment>

    private lateinit var batteryStateReceiver: BroadcastReceiver

    private var isFilterApplied: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        initNavigation()

        setupNavigation()
        setupViewPager()
        setupTabLayout()
        setupFab()
        attachObserver()
        loadCocktails()

        viewModel.testMediatorLiveData.observe(viewLifecycleOwner, Observer {  })
        setHasOptionsMenu(true)
        return viewDataBinding.root
    }

    override fun configureDataBinding() {
        super.configureDataBinding()
        viewDataBinding.viewModel = this.viewModel
    }

    private fun initNavigation() {
        (activity as AppCompatActivity).setSupportActionBar(viewDataBinding.hostToolbar)

        val navController = NavHostFragment.findNavController(this)

        NavigationUI.setupWithNavController(viewDataBinding.hostToolbar, navController)
    }

    private fun setupNavigation() {
        sharedHostViewModel.applyFilterEventLiveData.observe(viewLifecycleOwner, EventObserver {
            parentFragmentManager.popBackStack()
        })
    }

    private fun setupViewPager() {
        val historyFragment = CocktailsFragment.newInstance()
        val favoriteFragment = FavoriteFragment.newInstance()
        fragmentList = arrayListOf(historyFragment, favoriteFragment)

        val cocktailPagerAdapter = CocktailPagerAdapter(fragmentList, this)

        viewPager = viewDataBinding.vpMainFragment.apply {
            adapter = cocktailPagerAdapter
            offscreenPageLimit = 1
        }
    }

    private fun setupTabLayout() {
        val tabLayout = viewDataBinding.tlMainFragment
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.tl_host_fragment_history_tab)
                1 -> getString(R.string.tl_host_fragment_favorite_tab)
                else -> throw IllegalArgumentException("Unknown position found")
            }
        }.attach()
    }

    private fun setupFab() {
        viewDataBinding.searchFab.setOnClickListener {
            val action = HostFragmentDirections.actionHostFragmentToSearchCocktailsFragment()
            findNavController().navigate(action)
        }
    }

    private fun attachObserver() {
        sharedHostViewModel.filterResultLiveData.observe(viewLifecycleOwner, Observer {})
    }

    private fun loadCocktails() {
        sharedHostViewModel.loadCocktails()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.cocktail_fragment_menu, menu)

        val filterMenuItem = menu.findItem(R.id.menu_filter)
        val filterImageButton = ImageButton(activity)
        filterImageButton.setImageResource(R.drawable.ic_filter_list_24)
        filterImageButton.background = null
        filterMenuItem.actionView = filterImageButton
        filterMenuItem.actionView.setOnLongClickListener {
            sharedHostViewModel.resetFilters()
            true
        }
        filterMenuItem.actionView.setOnClickListener {
            menu.performIdentifierAction(R.id.menu_filter, 0)
        }

        val sortMenuItem = menu.findItem(R.id.menu_sort)
        val sortImageButton = ImageButton(activity)
        sortImageButton.setImageResource(R.drawable.ic_sort_24)
        sortImageButton.background = null
        sortMenuItem.actionView = sortImageButton
        sortMenuItem.actionView.setOnLongClickListener {
            sharedHostViewModel.sortingOrderLiveData.value = null
            true
        }
        sortMenuItem.actionView.setOnClickListener {
            menu.performIdentifierAction(R.id.menu_sort, 1)
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)

        val filterMenuItem: MenuItem = menu.findItem(R.id.menu_filter)
        if (isFilterApplied) {
            filterMenuItem.icon = ContextCompat
                .getDrawable(requireActivity(), R.drawable.ic_filter_list_24_indicator)
        } else {
            filterMenuItem.icon = ContextCompat
                .getDrawable(requireActivity(), R.drawable.ic_filter_list_24)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_filter -> {
                sharedHostViewModel.isFilterFragmentOpened = true
                val filterFragment = CocktailFilterFragment.newInstance()
                childFragmentManager.beginTransaction()
                    .add(R.id.filter_fragment_container, filterFragment)
                    .addToBackStack(null)
                    .commit()
                true
            }
            R.id.menu_sort -> {
                val sortKeyTypeList = CocktailSortType.values().map { it.key }.toTypedArray()
                MaterialAlertDialogBuilder(context)
                    .setTitle("Choose sort type")
                    .setItems(sortKeyTypeList) { _, i ->
                        sharedHostViewModel.sortingOrderLiveData.value = CocktailSortType.values()[i]
                    }.show()
                true
            }
            else -> false
        }
    }

    override fun onStart() {
        super.onStart()

        registerBatteryStatusReceiver()
    }

    private fun registerBatteryStatusReceiver() {
        batteryStateReceiver = BatteryStateReceiver(this)

        val intentFilter = IntentFilter().apply {
            addAction(Intent.ACTION_BATTERY_CHANGED)
            addAction(Intent.ACTION_BATTERY_OKAY)
            addAction(Intent.ACTION_BATTERY_LOW)
        }
        activity?.registerReceiver(batteryStateReceiver, intentFilter)
    }

    override fun onStop() {
        super.onStop()

        activity?.unregisterReceiver(batteryStateReceiver)
    }

    override fun updateBatteryState(batteryState: BatteryStateHolder) {
        viewDataBinding.viewModel?.updateBatteryState(batteryState)
    }

    private fun changeFilterIndicator(filterState: Boolean) {
        isFilterApplied = filterState
        activity?.invalidateOptionsMenu()
    }
}
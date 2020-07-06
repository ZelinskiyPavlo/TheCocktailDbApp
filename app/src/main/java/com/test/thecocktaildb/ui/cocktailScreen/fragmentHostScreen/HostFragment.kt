package com.test.thecocktaildb.ui.cocktailScreen.fragmentHostScreen

import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayoutMediator
import com.test.thecocktaildb.R
import com.test.thecocktaildb.databinding.FragmentHostBinding
import com.test.thecocktaildb.di.Injectable
import com.test.thecocktaildb.ui.base.BaseFragment
import com.test.thecocktaildb.ui.cocktailScreen.adapter.CocktailPagerAdapter
import com.test.thecocktaildb.ui.cocktailScreen.callback.BatteryStateCallback
import com.test.thecocktaildb.ui.cocktailScreen.favoriteScreen.FavoriteFragment
import com.test.thecocktaildb.ui.cocktailScreen.filterScreen.FilterFragment
import com.test.thecocktaildb.ui.cocktailScreen.historyScreen.HistoryFragment
import com.test.thecocktaildb.ui.cocktailScreen.sortType.CocktailSortType
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

    private lateinit var viewPager: ViewPager2
    private lateinit var fragmentList: ArrayList<Fragment>

    private lateinit var batteryStateReceiver: BroadcastReceiver

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        setupToolbar()

        setupNavigation()
        setupViewPager()
        setupTabLayout()
        setupFab()
        attachObserver()
        loadCocktails()

        return viewDataBinding.root
    }

    override fun configureDataBinding() {
        super.configureDataBinding()
        viewDataBinding.viewModel = this.viewModel
    }

    private fun setupToolbar() {
        viewDataBinding.hostFragmentToolbar.primaryOption.setOnClickListener {
            sharedHostViewModel.isFilterFragmentOpened = true
            val filterFragment = FilterFragment.newInstance()
            childFragmentManager.beginTransaction()
                .add(R.id.filter_fragment_container, filterFragment)
                .addToBackStack(null)
                .commit()
        }

        viewDataBinding.hostFragmentToolbar.secondaryOption.setOnClickListener {
            val sortKeyTypeList = CocktailSortType.values().map { it.key }.toTypedArray()
            MaterialAlertDialogBuilder(context)
                .setTitle("Choose sort type")
                .setItems(sortKeyTypeList) { _, i ->
                    sharedHostViewModel.sortingOrderLiveData.value = CocktailSortType.values()[i]
                }.show()
        }

        sharedHostViewModel.filterListLiveData.observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty() && it != listOf(null, null))
                viewDataBinding.hostFragmentToolbar
                    .changePrimaryOptionImage(R.drawable.ic_filter_list_24_indicator)
            else viewDataBinding.hostFragmentToolbar
                .changePrimaryOptionImage(R.drawable.ic_filter_list_24)
        })

        sharedHostViewModel.sortingOrderLiveData.observe(viewLifecycleOwner, Observer {
            if (it != null && it != CocktailSortType.RECENT)
                viewDataBinding.hostFragmentToolbar
                    .changeSecondaryOptionImage(R.drawable.ic_sort_24_indicator)
            else viewDataBinding.hostFragmentToolbar
                .changeSecondaryOptionImage(R.drawable.ic_sort_24)
        })

        viewDataBinding.hostFragmentToolbar.primaryOption.setOnLongClickListener {
            sharedHostViewModel.resetFilters()
            true
        }

        viewDataBinding.hostFragmentToolbar.secondaryOption.setOnLongClickListener {
            sharedHostViewModel.sortingOrderLiveData.value = null
            true
        }
    }

    private fun setupNavigation() {
        sharedHostViewModel.applyFilterEventLiveData.observe(viewLifecycleOwner, EventObserver {
            parentFragmentManager.popBackStack()
        })
    }

    private fun setupViewPager() {
        val historyFragment = HistoryFragment.newInstance()
        val favoriteFragment = FavoriteFragment.newInstance()
        fragmentList = arrayListOf(historyFragment, favoriteFragment)

        val cocktailPagerAdapter =
            CocktailPagerAdapter(
                fragmentList,
                this
            )

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
}
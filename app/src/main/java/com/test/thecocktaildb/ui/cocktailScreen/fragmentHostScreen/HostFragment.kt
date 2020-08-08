package com.test.thecocktaildb.ui.cocktailScreen.fragmentHostScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
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
import com.test.thecocktaildb.ui.cocktailScreen.favoriteScreen.FavoriteFragment
import com.test.thecocktaildb.ui.cocktailScreen.filterScreen.FilterFragment
import com.test.thecocktaildb.ui.cocktailScreen.historyScreen.HistoryFragment
import com.test.thecocktaildb.ui.cocktailScreen.sortType.CocktailSortType
import com.test.thecocktaildb.util.EventObserver
import com.test.thecocktaildb.util.HostViewModelFactory
import com.test.thecocktaildb.util.SavedStateViewModelFactory
import com.test.thecocktaildb.util.SharedHostViewModelFactory
import javax.inject.Inject

class HostFragment : BaseFragment<FragmentHostBinding>(), Injectable/*, BatteryStateCallback*/ {

    companion object {
        @JvmStatic
        fun newInstance(): HostFragment {
            return HostFragment()
        }
    }

    override val layoutId: Int = R.layout.fragment_host

    @Inject
    lateinit var hostViewModelFactory: HostViewModelFactory

    override val viewModel: HostViewModel by viewModels {
        SavedStateViewModelFactory(hostViewModelFactory, this)
    }

    @Inject
    lateinit var sharedHostViewModelFactory: SharedHostViewModelFactory

    private val sharedHostViewModel: SharedHostViewModel by activityViewModels {
        SavedStateViewModelFactory(sharedHostViewModelFactory, requireActivity())
    }

    private lateinit var viewPager: ViewPager2
    private lateinit var fragmentList: ArrayList<Fragment>

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
}
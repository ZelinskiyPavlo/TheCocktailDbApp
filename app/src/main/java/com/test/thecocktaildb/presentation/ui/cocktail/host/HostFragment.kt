package com.test.thecocktaildb.presentation.ui.cocktail.host

import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.text.bold
import androidx.core.text.color
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayoutMediator
import com.test.thecocktaildb.R
import com.test.thecocktaildb.core.common.firebase.Analytic
import com.test.thecocktaildb.databinding.FragmentHostBinding
import com.test.thecocktaildb.di.Injectable
import com.test.thecocktaildb.presentation.ui.base.BaseFragment
import com.test.thecocktaildb.presentation.ui.cocktail.adapter.viewpager.CocktailPagerAdapter
import com.test.thecocktaildb.presentation.ui.cocktail.favorite.FavoriteFragment
import com.test.thecocktaildb.presentation.ui.cocktail.filter.FilterFragment
import com.test.thecocktaildb.presentation.ui.cocktail.history.HistoryFragment
import com.test.thecocktaildb.presentation.ui.cocktail.sorttype.CocktailSortType
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
        setupObserver()

        return viewDataBinding.root
    }

    override fun configureDataBinding() {
        super.configureDataBinding()
        viewDataBinding.viewModel = this.viewModel
    }

    private fun setupToolbar() {
        viewDataBinding.hostFragmentToolbar.primaryOption.setOnClickListener {
            val filterFragment = FilterFragment.newInstance()
            childFragmentManager.beginTransaction()
                .add(R.id.filter_fragment_container, filterFragment)
                .addToBackStack(null)
                .commit()
        }

        viewDataBinding.hostFragmentToolbar.secondaryOption.setOnClickListener {
            val selectedColor = ContextCompat
                .getColor(requireActivity(), R.color.selected_cocktail_sort_type)
            val sortKeyTypeList = CocktailSortType.values().mapIndexed { index, sortType ->
                when {
                    index == 0 && sharedHostViewModel.sortingOrderLiveData.value == null -> {
                        SpannableStringBuilder()
                            .bold { color(selectedColor) { append(CocktailSortType.RECENT.key) } }
                    }
                    sharedHostViewModel.sortingOrderLiveData.value?.key == sortType.key -> {
                        SpannableStringBuilder()
                            .bold { color(selectedColor) { append(sortType.key) } }
                    }
                    else -> sortType.key
                }
            }.toTypedArray()
            MaterialAlertDialogBuilder(context)
                .setTitle(getString(R.string.dialog_cocktail_sorting_title))
                .setItems(sortKeyTypeList) { _, i ->
                    sharedHostViewModel.sortingOrderLiveData.value = CocktailSortType.values()[i]
                }.show()
        }

        sharedHostViewModel.filterListLiveData.observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty() && it != listOf(null, null, null))
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
            if (it != null && it.first.isNotEmpty()) {
                firebaseAnalytics.logEvent(
                    Analytic.COCKTAIL_FILTER_APPLY,
                    bundleOf(
                        Analytic.COCKTAIL_FILTER_APPLY_FILTER_TYPE_KEY to it.second.joinToString(),
                        Analytic.COCKTAIL_FILTER_APPLY_ALCOHOL_KEY to it.first[0],
                        Analytic.COCKTAIL_FILTER_APPLY_CATEGORY_KEY to it.first[1],
                        Analytic.COCKTAIL_FILTER_APPLY_INGREDIENT_KEY to it.first[2]
                    )
                )
            }

            parentFragmentManager.popBackStack()
        })
    }

    private fun setupViewPager() {
        val historyFragment = HistoryFragment.newInstance()
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

    private fun setupObserver() {
        sharedHostViewModel.filterResultLiveData.observe(viewLifecycleOwner, Observer {})

        sharedHostViewModel.favoriteStateChangedEventLiveData.observe(
            viewLifecycleOwner,
            EventObserver { triple ->
                val eventName = if (triple.first) Analytic.COCKTAIL_FAVORITE_ADD
                else Analytic.COCKTAIL_FAVORITE_REMOVE

                firebaseAnalytics.logEvent(
                    eventName,
                    bundleOf(
                        Analytic.COCKTAIL_FAVORITE_ID_KEY to triple.second,
                        Analytic.COCKTAIL_FAVORITE_USER_NAME_KEY to triple.third
                    )
                )
            })
    }
}
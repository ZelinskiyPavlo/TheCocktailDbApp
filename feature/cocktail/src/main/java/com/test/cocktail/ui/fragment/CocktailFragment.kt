package com.test.cocktail.ui.fragment

import android.content.BroadcastReceiver
import android.content.IntentFilter
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.text.bold
import androidx.core.text.color
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.analytics.FirebaseAnalytics
import com.test.cocktail.R
import com.test.cocktail.analytic.logCocktailFilterApply
import com.test.cocktail.analytic.logFavoriteCocktailStateChanged
import com.test.cocktail.databinding.FragmentCocktailBinding
import com.test.cocktail.factory.CocktailViewModelFactory
import com.test.cocktail.model.sorttype.CocktailSortType
import com.test.cocktail.navigation.CocktailNavigationApi
import com.test.cocktail.receiver.DrinkProposalReceiver
import com.test.cocktail.receiver.callback.DrinkProposalCallback
import com.test.cocktail.ui.CocktailViewModel
import com.test.cocktail.ui.CommunicationViewModel
import com.test.cocktail.ui.adapter.viewpager.CocktailPagerAdapter
import com.test.cocktail.ui.fragment.favorite.FavoriteFragment
import com.test.cocktail.ui.fragment.filter.FilterFragment
import com.test.cocktail.ui.fragment.history.HistoryFragment
import com.test.cocktail_common.service.ACTION_PROPOSE_DRINK
import com.test.navigation.HasBackPressLogic
import com.test.presentation.factory.SavedStateViewModelFactory
import com.test.presentation.ui.base.BaseFragment
import com.test.presentation.util.EventObserver
import java.util.*
import javax.inject.Inject

class CocktailFragment : BaseFragment<FragmentCocktailBinding>(), HasBackPressLogic, DrinkProposalCallback {

    companion object {
        @JvmStatic
        fun newInstance(): CocktailFragment {
            return CocktailFragment()
        }
    }

    override val layoutId: Int = R.layout.fragment_cocktail

    @Inject
    lateinit var viewModelFactory: CocktailViewModelFactory

    override val viewModel: CocktailViewModel by viewModels {
        // I can't use requireActivity for SavedStateRegistryOwner with scoping to fragment
        // bc it's broke application when restoring state. On the other hand savedState in CocktailVM
        // will only work when application is running but opened different fragment
        SavedStateViewModelFactory(viewModelFactory, this)
    }

    private val communicationViewModel: CommunicationViewModel by viewModels({ requireParentFragment() })

    @Inject
    lateinit var cocktailNavigator: CocktailNavigationApi

    @Inject
    lateinit var firebaseAnalytics: FirebaseAnalytics

    private val currentFragment: BaseFragment<*>?
        get() = childFragmentManager.findFragmentById(R.id.cocktail_fragment_container) as? BaseFragment<*>

    private lateinit var drinkProposalReceiver: BroadcastReceiver

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        setupObserver()
        setupToolbar()
        setupViewPager()
        setupTabLayout()

        return viewDataBinding.root
    }

    override fun configureDataBinding() {
        super.configureDataBinding()
        viewDataBinding.viewModel = viewModel
        viewDataBinding.fragment = this
    }

    private fun setupObserver() {
        viewModel.applyFiltersEventLiveData.observe(viewLifecycleOwner, EventObserver { selectedFilters ->
            firebaseAnalytics.logCocktailFilterApply(selectedFilters)

            closeFilterFragment()
        })
        viewModel.favoriteStateChangedEventLiveData.observe(
            viewLifecycleOwner,
            EventObserver { cocktailStateData ->
                firebaseAnalytics.logFavoriteCocktailStateChanged(cocktailStateData)
            })
    }

    private fun setupToolbar() {
        viewDataBinding.toolbar.primaryOption.setOnClickListener {
            navigateToFilterFragment()
        }

        viewDataBinding.toolbar.secondaryOption.setOnClickListener {
            val selectedColor = ContextCompat
                .getColor(requireActivity(), R.color.selected_cocktail_sort_type)
            val sortKeyTypeList = CocktailSortType.values().mapIndexed { index, sortType ->
                when {
                    index == 0 && viewModel.sortingOrderLiveData.value == null -> {
                        SpannableStringBuilder()
                            .bold { color(selectedColor) { append(CocktailSortType.RECENT.key) } }
                    }
                    viewModel.sortingOrderLiveData.value?.key == sortType.key -> {
                        SpannableStringBuilder()
                            .bold { color(selectedColor) { append(sortType.key) } }
                    }
                    else -> sortType.key
                }
            }.toTypedArray()
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(getString(R.string.dialog_cocktail_sorting_title))
                .setItems(sortKeyTypeList) { _, i ->
                    viewModel.sortingOrderLiveData.value = CocktailSortType.values()[i]
                }.show()
        }

        viewModel.filtersLiveData.observe(viewLifecycleOwner, {
            if (it.isNullOrEmpty().not().or(it != List(3) { null }))
                viewDataBinding.toolbar
                    .changePrimaryOptionImage(R.drawable.ic_filter_list_24_indicator)
            else viewDataBinding.toolbar
                .changePrimaryOptionImage(R.drawable.ic_filter_list_24)
        })

        viewModel.sortingOrderLiveData.observe(viewLifecycleOwner, {
            if (it != null && it != CocktailSortType.RECENT)
                viewDataBinding.toolbar
                    .changeSecondaryOptionImage(R.drawable.ic_sort_24_indicator)
            else viewDataBinding.toolbar
                .changeSecondaryOptionImage(R.drawable.ic_sort_24)
        })

        viewDataBinding.toolbar.primaryOption.setOnLongClickListener {
            viewModel.resetFilters()
            true
        }

        viewDataBinding.toolbar.secondaryOption.setOnLongClickListener {
            viewModel.sortingOrderLiveData.value = null
            true
        }
    }

    private fun setupViewPager() {
        val fragmentList: ArrayList<Fragment> = arrayListOf(
            HistoryFragment.newInstance(),
            FavoriteFragment.newInstance()
        )

        viewDataBinding.viewPager.apply {
            adapter = CocktailPagerAdapter(fragmentList, this@CocktailFragment)
            offscreenPageLimit = 1
        }
    }

    private fun setupTabLayout() {
        val tabLayout = viewDataBinding.tabLayout
        TabLayoutMediator(tabLayout, viewDataBinding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.tl_host_fragment_history_tab)
                1 -> getString(R.string.tl_host_fragment_favorite_tab)
                else -> throw IllegalArgumentException("Unknown position found")
            }
        }.attach()
    }

    fun onFabClicked() {
        cocktailNavigator.toCocktailSearch()
    }

    private fun navigateToFilterFragment() {
        val filterFragment = FilterFragment.newInstance()
        childFragmentManager.beginTransaction()
            .add(R.id.cocktail_fragment_container, filterFragment)
            .addToBackStack(null)
            .commit()
        communicationViewModel.onNestedFragmentNavigationLiveData.value = true
    }

    private fun closeFilterFragment() {
        communicationViewModel.onNestedFragmentNavigationLiveData.value = false
        childFragmentManager.popBackStack()
    }

    override fun onBackPressed() {
        if (currentFragment != null)
            closeFilterFragment()
        else cocktailNavigator.exit()
    }

    override fun onStart() {
        super.onStart()
        registerDrinkProposalReceiver()
    }

    override fun onStop() {
        super.onStop()
        activity?.unregisterReceiver(drinkProposalReceiver)
    }

    private fun registerDrinkProposalReceiver() {
        drinkProposalReceiver = DrinkProposalReceiver(this)

        val intentFilter = IntentFilter().apply {
            addAction(ACTION_PROPOSE_DRINK)
        }
        activity?.registerReceiver(drinkProposalReceiver, intentFilter)
    }

    override fun proposeCocktail(selectedCocktailId: Long) {
        if ((viewModel.cocktailsLiveData.value?.size ?: 0) > 1 && selectedCocktailId != -1L) {
            Snackbar.make(
                viewDataBinding.cocktailCoordinatorLayout,
                getString(R.string.snackbar_drink_proposal_title),
                Snackbar.LENGTH_LONG
            ).apply {
                setAction(getString(R.string.snackbar_button_show_proposed_cocktail)) {
                    viewModel.openProposedCocktail(selectedCocktailId)
                }
                animationMode = BaseTransientBottomBar.ANIMATION_MODE_SLIDE
                show()
            }
        }
    }
}
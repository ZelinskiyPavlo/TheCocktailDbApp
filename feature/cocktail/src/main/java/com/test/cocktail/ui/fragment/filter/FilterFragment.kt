package com.test.cocktail.ui.fragment.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.test.cocktail.R
import com.test.cocktail.databinding.FragmentFilterBinding
import com.test.cocktail.factory.CocktailViewModelFactory
import com.test.cocktail.ui.CocktailViewModel
import com.test.presentation.factory.SavedStateViewModelFactory
import com.test.presentation.model.cocktail.filter.DrinkFilter
import com.test.presentation.model.cocktail.filter.DrinkFilterType
import com.test.presentation.model.cocktail.type.CocktailAlcoholType
import com.test.presentation.model.cocktail.type.CocktailCategory
import com.test.presentation.model.cocktail.type.CocktailGlassType
import com.test.presentation.ui.base.BaseFragment
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

class FilterFragment : BaseFragment<FragmentFilterBinding>() {

    companion object {
        @JvmStatic
        fun newInstance(): FilterFragment {
            return FilterFragment()
        }
    }

    override val layoutId: Int = R.layout.fragment_filter

    @Inject
    internal lateinit var cocktailViewModelFactory: CocktailViewModelFactory

    private val cocktailViewModel: CocktailViewModel by viewModels({ requireParentFragment() }) {
        SavedStateViewModelFactory(cocktailViewModelFactory, requireParentFragment())
    }

    private lateinit var alcoholMenu: PopupMenu
    private lateinit var categoryMenu: PopupMenu
    private lateinit var glassMenu: PopupMenu

    private val chooseTextId = R.string.filter_fragment_choose_filter
    private val changeTextId = R.string.filter_fragment_change_filter
    private val emptyResultTextId = R.string.filter_fragment_snackbar_no_results
    private val resultsSignId = R.string.filter_fragment_results_sign

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        setupToolbar()
        setupFilterPopMenu()
        setupFilterButtons()

        return viewDataBinding.root
    }

    override fun configureDataBinding() {
        super.configureDataBinding()
        viewDataBinding.viewModel = cocktailViewModel
    }

    override fun setupObservers() {
        super.setupObservers()
        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
//                cocktailViewModel.eventsFlow.onEach { event ->
//                    when (event) {
//                        is CocktailViewModel.Event.FilterResult -> showFilterResultSnackbar("Test result")
//                    }
//                }.launchIn(this)
                cocktailViewModel.filterResultFlow.onEach { resultPair ->
                    showFilterResultSnackbar(generateResultSign(resultPair))
                }.launchIn(this)

                cocktailViewModel.alcoholSignFlow.onEach { sign ->
                    viewDataBinding.filterBtnAlcohol.text = generateSign(sign)
                }.launchIn(this)

                cocktailViewModel.categorySignFlow.onEach { sign ->
                    viewDataBinding.filterBtnCategory.text = generateSign(sign)
                }.launchIn(this)

                cocktailViewModel.glassSignFlow.onEach { sign ->
                    viewDataBinding.filterBtnGlass.text = generateSign(sign)
                }.launchIn(this)
            }
        }
    }

    private fun generateSign(input: String?): String {
        // TODO: 05.11.2021 remove hardcoded whitespaces
        return if (input == null) getString(chooseTextId)
        else getString(changeTextId, input)
    }

    private fun generateResultSign(input: Pair<Int, Int>?): String {
        return if (input == null) {
            getString(emptyResultTextId)
        } else {
            getString(resultsSignId, input.first, input.second)
        }
    }

    private fun setupToolbar() {
        viewDataBinding.filterFragmentToolbar.primaryOption.visibility = View.GONE
        viewDataBinding.filterFragmentToolbar.secondaryOption.visibility = View.GONE
    }

    private fun setupFilterPopMenu() {
        fun populateMenu(drinkFilterList: Array<out DrinkFilter>, popupMenu: PopupMenu) {
            drinkFilterList.dropLast(1).forEachIndexed { index, drinkFilter ->
                popupMenu.menu.add(
                    Menu.NONE, index, Menu.NONE, drinkFilter.key
                        .replace("_", " ")
                        .replace("\\/", "")
                )
            }
        }

        alcoholMenu = PopupMenu(context, viewDataBinding.filterBtnAlcohol)
        categoryMenu = PopupMenu(context, viewDataBinding.filterBtnCategory)
        glassMenu = PopupMenu(context, viewDataBinding.filterBtnGlass)

        val alcoholDrinkFilter = CocktailAlcoholType.values()
        val categoryDrinkFilter = CocktailCategory.values()
        val glassDrinkFilter = CocktailGlassType.values()

        populateMenu(alcoholDrinkFilter, alcoholMenu)
        populateMenu(categoryDrinkFilter, categoryMenu)
        populateMenu(glassDrinkFilter, glassMenu)

        alcoholMenu.setOnMenuItemClickListener { menuItem ->
            cocktailViewModel.addFilter(menuItem.itemId, DrinkFilterType.ALCOHOL)
            true
        }
        categoryMenu.setOnMenuItemClickListener { menuItem ->
            cocktailViewModel.addFilter(menuItem.itemId, DrinkFilterType.CATEGORY)
            true
        }
        glassMenu.setOnMenuItemClickListener { menuItem ->
            cocktailViewModel.addFilter(menuItem.itemId, DrinkFilterType.GLASS)
            true
        }
    }

    private fun setupFilterButtons() {
        viewDataBinding.filterBtnAlcohol.setOnClickListener { alcoholMenu.show() }
        viewDataBinding.filterBtnCategory.setOnClickListener { categoryMenu.show() }
        viewDataBinding.filterBtnGlass.setOnClickListener { glassMenu.show() }
    }

    // TODO: 06.11.2021 Remove flickering, see:
    //  https://stackoverflow.com/questions/61224010/snackbar-flickers-if-androidanimatelayoutchanges-is-true-on-root-layout
    private fun showFilterResultSnackbar(message: String) {
        Snackbar.make(viewDataBinding.root, message, Snackbar.LENGTH_SHORT)
            .apply {
                setAction(getString(R.string.filter_fragment_undo_filters)) {
                    cocktailViewModel.resetFilters()
                }
                animationMode = BaseTransientBottomBar.ANIMATION_MODE_SLIDE
                show()
            }
    }
}
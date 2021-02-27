package com.test.cocktail.ui.fragment.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.viewModels
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
import com.test.presentation.util.EventObserver
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        setupToolbar()
        setupFilterPopMenu()
        setupFilterButtons()
        setupResultSnackbar()
        setInitialText()
        
        return viewDataBinding.root
    }

    override fun configureDataBinding() {
        super.configureDataBinding()
        viewDataBinding.viewModel = cocktailViewModel
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
            cocktailViewModel.filterSpecified(menuItem.itemId, DrinkFilterType.ALCOHOL)
            true
        }
        categoryMenu.setOnMenuItemClickListener { menuItem ->
            cocktailViewModel.filterSpecified(menuItem.itemId, DrinkFilterType.CATEGORY)
            true
        }
        glassMenu.setOnMenuItemClickListener { menuItem ->
            cocktailViewModel.filterSpecified(menuItem.itemId, DrinkFilterType.GLASS)
            true
        }
    }

    private fun setupFilterButtons() {
        viewDataBinding.filterBtnAlcohol.setOnClickListener { alcoholMenu.show() }
        viewDataBinding.filterBtnCategory.setOnClickListener { categoryMenu.show() }
        viewDataBinding.filterBtnGlass.setOnClickListener { glassMenu.show() }
    }

    // TODO: 24.02.2021 Fix bug with flickering when showing snackbar
    //  (maybe some race condition happen)
    private fun setupResultSnackbar() {
        cocktailViewModel.filterResultLiveData.observe(
            viewLifecycleOwner,
            EventObserver { message ->
                Snackbar.make(viewDataBinding.root, message, Snackbar.LENGTH_SHORT)
                    .apply {
                        setAction(getString(R.string.filter_fragment_undo_filters)) {
                            cocktailViewModel.resetFilters()
                        }
                        animationMode = BaseTransientBottomBar.ANIMATION_MODE_SLIDE
                        show()
                    }
            })
    }

    private fun setInitialText() {
        val chooseText = getString(R.string.filter_fragment_choose_filter)
        val changeText = getString(R.string.filter_fragment_change_filter)
        val emptyResultText = getString(R.string.filter_fragment_snackbar_no_results)
        val resultsSign = getString(R.string.filter_fragment_results_sign)

        if(cocktailViewModel.alcoholSignLiveData.value == null &&
            cocktailViewModel.categorySignLiveData.value == null){
            cocktailViewModel.setInitialText(chooseText, changeText, emptyResultText, resultsSign)
        }
    }
}
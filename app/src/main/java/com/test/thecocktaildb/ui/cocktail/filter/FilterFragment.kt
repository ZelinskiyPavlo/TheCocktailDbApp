package com.test.thecocktaildb.ui.cocktail.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.activityViewModels
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.test.thecocktaildb.R
import com.test.thecocktaildb.databinding.FragmentFilterBinding
import com.test.thecocktaildb.di.Injectable
import com.test.thecocktaildb.presentationNew.model.CocktailAlcoholType
import com.test.thecocktaildb.presentationNew.model.CocktailCategory
import com.test.thecocktaildb.ui.base.BaseFragment
import com.test.thecocktaildb.ui.cocktail.filtertype.DrinkFilter
import com.test.thecocktaildb.ui.cocktail.filtertype.DrinkFilterType
import com.test.thecocktaildb.ui.cocktail.filtertype.IngredientDrinkFilter
import com.test.thecocktaildb.ui.cocktail.host.SharedHostViewModel
import com.test.thecocktaildb.util.EventObserver
import com.test.thecocktaildb.util.SavedStateViewModelFactory
import com.test.thecocktaildb.util.SharedHostViewModelFactory
import javax.inject.Inject

class FilterFragment : BaseFragment<FragmentFilterBinding>(), Injectable {

    companion object {
        @JvmStatic
        fun newInstance(): FilterFragment {
            return FilterFragment()
        }
    }

    override val layoutId: Int = R.layout.fragment_filter

    @Inject
    lateinit var sharedHostViewModelFactory: SharedHostViewModelFactory

    private val sharedHostViewModel: SharedHostViewModel by activityViewModels {
        SavedStateViewModelFactory(sharedHostViewModelFactory, requireActivity(), null)
    }

    private lateinit var alcoholMenu: PopupMenu
    private lateinit var categoryMenu: PopupMenu
    private lateinit var ingredientMenu: PopupMenu

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
        viewDataBinding.sharedViewModel = sharedHostViewModel
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
        ingredientMenu = PopupMenu(context, viewDataBinding.filterBtnIngredient)

        val alcoholDrinkFilter = CocktailAlcoholType.values()
        val categoryDrinkFilter = CocktailCategory.values()
        val ingredientDrinkFilter = IngredientDrinkFilter.values().dropLast(1).toTypedArray()

        populateMenu(alcoholDrinkFilter, alcoholMenu)
        populateMenu(categoryDrinkFilter, categoryMenu)
        populateMenu(ingredientDrinkFilter, ingredientMenu)

        alcoholMenu.setOnMenuItemClickListener { menuItem ->
            sharedHostViewModel.filterSpecified(menuItem.itemId, DrinkFilterType.ALCOHOL)
            true
        }
        categoryMenu.setOnMenuItemClickListener { menuItem ->
            sharedHostViewModel.filterSpecified(menuItem.itemId, DrinkFilterType.CATEGORY)
            true
        }
        ingredientMenu.setOnMenuItemClickListener { menuItem ->
            sharedHostViewModel.filterSpecified(menuItem.itemId, DrinkFilterType.INGREDIENT)
            true
        }
    }

    private fun setupFilterButtons() {
        viewDataBinding.filterBtnAlcohol.setOnClickListener { alcoholMenu.show() }
        viewDataBinding.filterBtnCategory.setOnClickListener { categoryMenu.show() }
        viewDataBinding.filterBtnIngredient.setOnClickListener { ingredientMenu.show() }
    }

    private fun setupResultSnackbar() {
        sharedHostViewModel.filterResultLiveData.observe(
            viewLifecycleOwner,
            EventObserver { message ->
                Snackbar.make(viewDataBinding.root, message, Snackbar.LENGTH_SHORT)
                    .apply {
                        setAction(getString(R.string.filter_fragment_undo_filters)) {
                            sharedHostViewModel.resetFilters()
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

        if(sharedHostViewModel.alcoholSignLiveData.value == null &&
            sharedHostViewModel.categorySignLiveData.value == null){
            sharedHostViewModel.setInitialText(chooseText, changeText, emptyResultText)
        }
    }
}
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
import com.test.thecocktaildb.databinding.CocktailFilterFragmentBinding
import com.test.thecocktaildb.ui.base.BaseFragment
import com.test.thecocktaildb.ui.cocktail.filtertype.AlcoholDrinkFilter
import com.test.thecocktaildb.ui.cocktail.filtertype.CategoryDrinkFilter
import com.test.thecocktaildb.ui.cocktail.filtertype.DrinkFilter
import com.test.thecocktaildb.ui.cocktail.filtertype.DrinkFilterType
import com.test.thecocktaildb.ui.cocktail.host.SharedHostViewModel
import com.test.thecocktaildb.util.EventObserver

class CocktailFilterFragment :
    BaseFragment<CocktailFilterFragmentBinding, CocktailFilterViewModel>() {

    companion object {
        fun newInstance(): CocktailFilterFragment {
            return CocktailFilterFragment()
        }
    }

    override val layoutId: Int = R.layout.cocktail_filter_fragment

    override fun getViewModelClass(): Class<CocktailFilterViewModel> =
        CocktailFilterViewModel::class.java

    private val sharedHostViewModel: SharedHostViewModel by activityViewModels { delegatedViewModelFactory }

    private lateinit var alcoholMenu: PopupMenu
    private lateinit var categoryMenu: PopupMenu

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        attachBindingVariable()
        setupToolbar()

        setupFilterPopMenu()
        setupFilterButtons()
        setupResultSnackbar()
        return viewDataBinding.root
    }

    private fun attachBindingVariable() {
        viewDataBinding.sharedViewModel = sharedHostViewModel
    }

    private fun setupToolbar() {
        viewDataBinding.filterFragmentToolbar.backButton.visibility = View.GONE
        viewDataBinding.filterFragmentToolbar.primaryOption.visibility = View.GONE
        viewDataBinding.filterFragmentToolbar.secondaryOption.visibility = View.GONE
    }

    private fun setupFilterPopMenu() {
        val chooseText = "Обрати"
        val changeText = "Змінити"
        if(sharedHostViewModel.alcoholSignLiveData.value == null &&
                sharedHostViewModel.categorySignLiveData.value == null){
            sharedHostViewModel.setInitialText(chooseText, changeText)
        }

        fun populateMenu(drinkFilterList: Array<out DrinkFilter>, popupMenu: PopupMenu) {
            drinkFilterList.forEachIndexed { index, drinkFilter ->
                popupMenu.menu.add(
                    Menu.NONE, index, Menu.NONE, drinkFilter.key
                        .replace("_", " ")
                        .replace("\\/", "")
                )
            }
        }

        alcoholMenu = PopupMenu(context, viewDataBinding.filterBtnAlcohol)
        categoryMenu = PopupMenu(context, viewDataBinding.filterBtnCategory)

        val alcoholDrinkFilter = AlcoholDrinkFilter.values()
        val categoryDrinkFilter = CategoryDrinkFilter.values()

        populateMenu(alcoholDrinkFilter, alcoholMenu)
        populateMenu(categoryDrinkFilter, categoryMenu)

        alcoholMenu.setOnMenuItemClickListener { menuItem ->
            sharedHostViewModel.filterSpecified(menuItem.itemId, DrinkFilterType.ALCOHOL)
            true
        }
        categoryMenu.setOnMenuItemClickListener { menuItem ->
            sharedHostViewModel.filterSpecified(menuItem.itemId, DrinkFilterType.CATEGORY)
            true
        }
    }

    private fun setupFilterButtons() {
        viewDataBinding.filterBtnAlcohol.setOnClickListener { alcoholMenu.show() }
        viewDataBinding.filterBtnCategory.setOnClickListener { categoryMenu.show() }
    }

    private fun setupResultSnackbar() {
        sharedHostViewModel.filterResultLiveData.observe(
            viewLifecycleOwner,
            EventObserver { message ->
                Snackbar.make(viewDataBinding.root, message, Snackbar.LENGTH_SHORT)
                    .apply {
                        setAction("UNDO") {
                            sharedHostViewModel.resetFilters()
                        }
                        animationMode = BaseTransientBottomBar.ANIMATION_MODE_SLIDE
                        show()
                    }
            })
    }
}
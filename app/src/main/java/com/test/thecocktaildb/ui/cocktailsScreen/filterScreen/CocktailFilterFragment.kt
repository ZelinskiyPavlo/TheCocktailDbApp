package com.test.thecocktaildb.ui.cocktailsScreen.filterScreen

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import com.test.thecocktaildb.R
import com.test.thecocktaildb.databinding.CocktailFilterFragmentBinding
import com.test.thecocktaildb.ui.base.BaseFragment
import com.test.thecocktaildb.ui.cocktailsScreen.callback.FragmentEventCallback
import com.test.thecocktaildb.ui.cocktailsScreen.drinkFilter.AlcoholDrinkFilter
import com.test.thecocktaildb.ui.cocktailsScreen.drinkFilter.CategoryDrinkFilter
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

    private lateinit var fragmentEventCallback: FragmentEventCallback

    private lateinit var alcoholMenu: PopupMenu
    private lateinit var categoryMenu: PopupMenu

    override fun onAttach(context: Context) {
        super.onAttach(context)
        fragmentEventCallback = context as FragmentEventCallback
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        attachBindingVariable()

        setupFilterPopMenu()
        setupFilterButtons()
        return viewDataBinding.root
    }

    private fun attachBindingVariable() {
        viewDataBinding.viewModel = viewModel
    }

    private fun setupFilterPopMenu() {
        val chooseText = "Обрати"
        val changeText = "Змінити"
        viewDataBinding.viewModel?.setInitialText(chooseText, changeText)

        alcoholMenu = PopupMenu(context, viewDataBinding.filterBtnAlcohol)
        categoryMenu = PopupMenu(context, viewDataBinding.filterBtnCategory)

        val alcoholDrinkFilter = AlcoholDrinkFilter.values()
        val categoryDrinkFilter = CategoryDrinkFilter.values()

        alcoholDrinkFilter.forEachIndexed { index, drinkFilter ->
            alcoholMenu.menu.add(
                Menu.NONE, index, Menu.NONE, drinkFilter.key
                    .replace("_", " ")
                    .replace("\\/", "")
            )
        }
        categoryDrinkFilter.forEachIndexed { index, drinkFilter ->
            categoryMenu.menu.add(
                Menu.NONE, index, Menu.NONE, drinkFilter.key
                    .replace("_", " ")
                    .replace("\\/", "")
            )
        }

        alcoholMenu.setOnMenuItemClickListener { menuItem ->
            viewDataBinding.viewModel?.alcoholFilterSpecified(menuItem.itemId)
            true
        }
        categoryMenu.setOnMenuItemClickListener { menuItem ->
            viewDataBinding.viewModel?.categoryFilterSpecified(menuItem.itemId)
            true
        }
    }

    private fun setupFilterButtons() {
        viewDataBinding.filterBtnAlcohol.setOnClickListener { alcoholMenu.show() }
        viewDataBinding.filterBtnCategory.setOnClickListener { categoryMenu.show() }

        viewModel.clearFilterEventLiveData.observe(viewLifecycleOwner, EventObserver {
            viewModel.selectedFilterTypeList = mutableListOf(null)
        })

        viewModel.applyFilterEventLiveData.observe(viewLifecycleOwner, EventObserver {
            fragmentEventCallback.navigateToHostFragmentEvent(it)
        })
    }
}
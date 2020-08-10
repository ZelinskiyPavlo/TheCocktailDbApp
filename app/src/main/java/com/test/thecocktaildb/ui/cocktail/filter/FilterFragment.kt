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
import com.test.thecocktaildb.presentationNew.model.cocktail.CocktailAlcoholType
import com.test.thecocktaildb.presentationNew.model.cocktail.CocktailCategory
import com.test.thecocktaildb.ui.base.BaseFragment
import com.test.thecocktaildb.ui.cocktail.filtertype.DrinkFilter
import com.test.thecocktaildb.ui.cocktail.filtertype.DrinkFilterType
import com.test.thecocktaildb.ui.cocktail.host.SharedHostViewModel
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        setupFilterPopMenu()
        setupFilterButtons()
        setupResultSnackbar()
        return viewDataBinding.root
    }

    override fun configureDataBinding() {
        super.configureDataBinding()
        viewDataBinding.sharedViewModel = sharedHostViewModel
    }

    private fun setupFilterPopMenu() {
        val chooseText = "Обрати"
        val changeText = "Змінити"
        if(sharedHostViewModel.alcoholSignLiveData.value == null &&
                sharedHostViewModel.categorySignLiveData.value == null){
            sharedHostViewModel.setInitialText(chooseText, changeText)
        }

        fun populateMenu(drinkFilterList: Array<out DrinkFilter>, popupMenu: PopupMenu) {
            drinkFilterList.dropLast(1).forEachIndexed { index, drinkFilter ->
                popupMenu.menu.add(
                    Menu.NONE, index, Menu.NONE, drinkFilter.key
                )
            }
        }

        alcoholMenu = PopupMenu(context, viewDataBinding.filterBtnAlcohol)
        categoryMenu = PopupMenu(context, viewDataBinding.filterBtnCategory)

        val alcoholDrinkFilter = CocktailAlcoholType.values()
        val categoryDrinkFilter = CocktailCategory.values()

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
            { message ->
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
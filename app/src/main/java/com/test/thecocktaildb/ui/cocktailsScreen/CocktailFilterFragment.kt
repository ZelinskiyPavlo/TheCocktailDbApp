package com.test.thecocktaildb.ui.cocktailsScreen

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import com.test.thecocktaildb.R
import com.test.thecocktaildb.databinding.CocktailFilterFragmentBinding
import com.test.thecocktaildb.ui.base.BaseFragment
import com.test.thecocktaildb.ui.cocktailsScreen.drinkFilter.AlcoholDrinkFilter
import com.test.thecocktaildb.ui.cocktailsScreen.drinkFilter.CategoryDrinkFilter
import com.test.thecocktaildb.ui.cocktailsScreen.drinkFilter.DrinkFilter
import com.test.thecocktaildb.ui.cocktailsScreen.drinkFilter.DrinkFilterType
import com.test.thecocktaildb.util.EventObserver

class CocktailFilterFragment : BaseFragment<CocktailFilterFragmentBinding, CocktailFilterViewModel>() {

    companion object {
        fun newInstance(drinkFilterTypeList: List<DrinkFilterType>? = null): CocktailFilterFragment {
            val filterFragment = CocktailFilterFragment()
            val args = Bundle()

            if (drinkFilterTypeList != null) {
                drinkFilterTypeList.forEach { drinkFilterType ->
                    args.putSerializable(drinkFilterType.toString(), drinkFilterType)
                }
                filterFragment.arguments = args
            }
            return filterFragment
        }
    }

    private lateinit var fragmentNavigationListener: FragmentNavigationListener

    private lateinit var radioGroupList: List<RadioGroup>

    override val layoutId: Int = R.layout.cocktail_filter_fragment

    override fun getViewModelClass(): Class<CocktailFilterViewModel> =
        CocktailFilterViewModel::class.java

    override fun onAttach(context: Context) {
        super.onAttach(context)
        fragmentNavigationListener = context as FragmentNavigationListener
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        attachBindingVariable()

        setupRadioGroup()
        setupFilterButtons()
        return viewDataBinding.root
    }

    private fun attachBindingVariable() {
        viewDataBinding.viewModel = viewModel
    }

    private fun setupRadioGroup() {
        radioGroupList =
            listOf(viewDataBinding.radioGroupFilter1, viewDataBinding.radioGroupFilter2)

        fun populateRadioGroup(radioGroupIndex: Int, filterValues: Array<out DrinkFilter>) {
            filterValues.forEach {
                val radioButton = RadioButton(activity)
                radioButton.text = it.key.replace("_", " ")
                radioGroupList[radioGroupIndex].addView(radioButton)
            }
            val radioButton = RadioButton(activity)
            radioButton.text = "None"
            radioGroupList[radioGroupIndex].addView(radioButton)
        }

        viewDataBinding.viewModel?.drinkFilterTypeList =
            DrinkFilterType.values().mapIndexed { index, drinkFilterType ->
                when (arguments?.get(drinkFilterType.toString())) {
                    DrinkFilterType.ALCOHOL -> {
                        populateRadioGroup(index, AlcoholDrinkFilter.values())
                        DrinkFilterType.ALCOHOL
                    }
                    DrinkFilterType.CATEGORY -> {
                        populateRadioGroup(index, CategoryDrinkFilter.values())
                        DrinkFilterType.CATEGORY
                    }
                    else -> null
                }
            }

        viewDataBinding.viewModel?.retrieveFilterEventLiveData?.observe(viewLifecycleOwner,
            EventObserver {
                viewDataBinding.viewModel?.selectedFilterViewIdList = radioGroupList.map {
                    it.checkedRadioButtonId
                }
            })
    }

    private fun setupFilterButtons() {
        viewModel.clearFilterEventLiveData.observe(viewLifecycleOwner, EventObserver {
            viewDataBinding.radioGroupFilter1.clearCheck()
            viewDataBinding.radioGroupFilter2.clearCheck()
            viewModel.selectedFilterTypeList = listOf(null)
        })

        viewModel.applyFilterEventLiveData.observe(viewLifecycleOwner, EventObserver {
            fragmentNavigationListener.navigateToCocktailFragment(it)
        })
    }
}
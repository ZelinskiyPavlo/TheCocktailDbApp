package com.test.thecocktaildb.ui.cocktailDetailsScreen

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.test.thecocktaildb.R
import com.test.thecocktaildb.databinding.CocktailDetailsFragmentBinding
import com.test.thecocktaildb.di.Injectable
import com.test.thecocktaildb.ui.base.BaseFragment
import com.test.thecocktaildb.util.service.DrinkProposalService

class CocktailDetailsFragment : Injectable,
    BaseFragment<CocktailDetailsFragmentBinding, CocktailDetailsViewModel>() {

    override val layoutId: Int = R.layout.cocktail_details_fragment

    override fun getViewModelClass(): Class<CocktailDetailsViewModel> =
        CocktailDetailsViewModel::class.java

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        attachBindingVariable()

        setupIngredientsRecyclerView()

        getCocktail()
        return viewDataBinding.root
    }

    private fun attachBindingVariable() {
        viewDataBinding.viewModel = viewModel
    }

    private fun setupIngredientsRecyclerView() {
        val ingredientsAdapter = IngredientsAdapter()
        viewDataBinding.ingredientsRv.apply {
            adapter = ingredientsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    private fun getCocktail() {
        val cocktailId = getNavigationArgs()
        viewModel.getCocktailById(cocktailId)
    }

    private fun getNavigationArgs(): String {
        val args by navArgs<CocktailDetailsFragmentArgs>()

        return args.cocktailId
    }

    override fun onDestroyView() {
        super.onDestroyView()

        launchDrinkProposalService()
    }

    private fun launchDrinkProposalService() {
        val intentWithCocktail = Intent(activity, DrinkProposalService::class.java)
        val selectedCocktailId = viewDataBinding.viewModel?.cocktailId
        intentWithCocktail.putExtra(Intent.EXTRA_TEXT, selectedCocktailId)

        activity?.startService(intentWithCocktail)
    }
}
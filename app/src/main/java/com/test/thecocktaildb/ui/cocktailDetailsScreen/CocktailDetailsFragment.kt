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
import timber.log.Timber

class CocktailDetailsFragment : Injectable,
    BaseFragment<CocktailDetailsFragmentBinding, CocktailDetailsViewModel>() {

    override fun getLayoutId(): Int = R.layout.cocktail_details_fragment

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
        return mViewDataBinding.root
    }

    private fun attachBindingVariable() {
        mViewDataBinding.viewModel = mViewModel
    }

    private fun setupIngredientsRecyclerView() {
        val ingredientsAdapter = IngredientsAdapter()
        mViewDataBinding.ingredientsRv.apply {
            adapter = ingredientsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    private fun getCocktail() {
        val cocktailId = getNavigationArgs()
        mViewModel.getCocktailById(cocktailId)
    }

    private fun getNavigationArgs(): String {
        val args by navArgs<CocktailDetailsFragmentArgs>()

        return args.cocktailId
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Timber.d("onDestroyView in Fragment")

        launchDrinkProposalService()
    }

    private fun launchDrinkProposalService(){
        val intentWithCocktail = Intent(activity, DrinkProposalService::class.java)
        val stubId = 1
//        TODO: does name needs to be "SomeName" or "SOME_NAME"???
//        TODO: do i need to extract intent name to top-level declaration???
        intentWithCocktail.putExtra("SelectedDrink", stubId)

        activity?.startService(Intent(activity, DrinkProposalService::class.java))
    }
}
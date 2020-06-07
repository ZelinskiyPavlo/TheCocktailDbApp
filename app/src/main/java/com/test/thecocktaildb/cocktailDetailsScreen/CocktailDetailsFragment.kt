package com.test.thecocktaildb.cocktailDetailsScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.test.thecocktaildb.R
import com.test.thecocktaildb.base.BaseFragment
import com.test.thecocktaildb.databinding.CocktailDetailsFragmentBinding
import com.test.thecocktaildb.di.Injectable

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

}
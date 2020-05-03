package com.test.thecocktaildb.cocktailDetailsScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.test.thecocktaildb.R
import com.test.thecocktaildb.databinding.CocktailDetailsFragmentBinding
import com.test.thecocktaildb.databinding.SearchCocktailsFragmentBinding
import com.test.thecocktaildb.di.Injectable
import com.test.thecocktaildb.searchCocktailsScreen.SearchCocktailsViewModel
import javax.inject.Inject

class CocktailDetailsFragment: Injectable, Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var mViewModel: CocktailDetailsViewModel

    private lateinit var viewDataBinding: CocktailDetailsFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        setupViewModel()

        setupDataBinding(inflater, container)

        getCocktail()
        return viewDataBinding.root
    }

    private fun setupViewModel() {
        mViewModel =
            ViewModelProvider(this, viewModelFactory)[CocktailDetailsViewModel::class.java]
    }

    private fun setupDataBinding(inflater: LayoutInflater, container: ViewGroup?){
        viewDataBinding = CocktailDetailsFragmentBinding.inflate(inflater, container, false)
            .apply {
                viewModel = mViewModel
            }
        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner
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
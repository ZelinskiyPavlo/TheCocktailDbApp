package com.test.thecocktaildb.ui.cocktailsScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.GridLayoutManager
import com.test.thecocktaildb.R
import com.test.thecocktaildb.databinding.CocktailsFragmentBinding
import com.test.thecocktaildb.di.Injectable
import com.test.thecocktaildb.ui.base.BaseFragment
import com.test.thecocktaildb.util.EventObserver

class CocktailsFragment : Injectable, BaseFragment<CocktailsFragmentBinding, CocktailsViewModel>() {

    override fun getLayoutId(): Int = R.layout.cocktails_fragment

    override fun getViewModelClass() = CocktailsViewModel::class.java

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        attachBindingVariable()
        initNavigation()

        setupFab()
        setupNavigation()
        setupRecyclerView()
        loadCocktails()

        return mViewDataBinding.root
    }

    private fun initNavigation() {
        (activity as AppCompatActivity).setSupportActionBar(mViewDataBinding.cocktailToolbar)

        val navController = NavHostFragment.findNavController(this)

        NavigationUI.setupWithNavController(mViewDataBinding.cocktailToolbar, navController)
    }

    private fun attachBindingVariable(){
        mViewDataBinding.viewModel = mViewModel
    }

    private fun setupFab(){
        mViewDataBinding.searchFab.setOnClickListener {
            val action = CocktailsFragmentDirections
                .actionCocktailsFragmentToSearchCocktailsFragment()
            findNavController().navigate(action)
        }
    }

    private fun setupNavigation(){
        mViewDataBinding.viewModel?.cocktailDetailsEvent?.observe(viewLifecycleOwner, EventObserver{
            val (actionBarTitle, cocktailId) = it
            val action = CocktailsFragmentDirections
                .actionCocktailsFragmentToCocktailDetailsFragment(actionBarTitle, cocktailId)
            findNavController().navigate(action)
        })
    }

    private fun setupRecyclerView(){
        val cocktailsAdapter = CocktailsAdapter(mViewModel)
        mViewDataBinding.cocktailsRv.apply {
            adapter = cocktailsAdapter
            layoutManager = GridLayoutManager(activity, 2)
        }
    }

    private fun loadCocktails(){
        mViewModel.loadCocktails()
    }

}
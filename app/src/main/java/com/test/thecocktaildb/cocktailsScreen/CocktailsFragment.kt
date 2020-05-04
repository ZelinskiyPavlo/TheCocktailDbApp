package com.test.thecocktaildb.cocktailsScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.test.thecocktaildb.R
import com.test.thecocktaildb.databinding.CocktailsFragmentBinding
import com.test.thecocktaildb.di.Injectable
import com.test.thecocktaildb.utils.EventObserver
import timber.log.Timber
import javax.inject.Inject

class CocktailsFragment : Injectable, Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var mViewModel: CocktailsViewModel

    private lateinit var viewDataBinding: CocktailsFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupViewModel()

        setupDataBinding(inflater, container)

        setupFab()
        setupNavigation()
        setupRecyclerView()
        loadCocktails()

        return viewDataBinding.root
    }

    private fun setupViewModel(){
        mViewModel = ViewModelProvider(this, viewModelFactory)[CocktailsViewModel::class.java]
    }

    private fun setupDataBinding(inflater: LayoutInflater, container: ViewGroup?){
        viewDataBinding = CocktailsFragmentBinding.inflate(inflater, container, false)
            .apply {
            }
        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner
    }

    private fun setupFab(){
        viewDataBinding.searchFab.setOnClickListener {
            val action = CocktailsFragmentDirections
                .actionCocktailsFragmentToSearchCocktailsFragment()
            findNavController().navigate(action)
        }
    }

    private fun setupNavigation(){
        viewDataBinding.viewModel?.cocktailDetailEvent?.observe(viewLifecycleOwner, EventObserver{
            val (actionBarTitle, cocktailId) = it
            val action = CocktailsFragmentDirections
                .actionCocktailsFragmentToCocktailDetailsFragment(actionBarTitle, cocktailId)
            findNavController().navigate(action)
        })
    }

    private fun setupRecyclerView(){
//        val cocktailsAdapter = CocktailsAdapter(viewDataBinding.viewModel)
        val cocktailsAdapter = CocktailsAdapter(mViewModel)
        viewDataBinding.cocktailsRv.apply {
            adapter = cocktailsAdapter
            layoutManager = LinearLayoutManager(activity)
        }


    }

    private fun loadCocktails(){

    }

}
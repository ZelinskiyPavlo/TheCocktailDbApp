package com.test.thecocktaildb.ui.cocktail.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.test.thecocktaildb.R
import com.test.thecocktaildb.databinding.FavoriteFragmentBinding
import com.test.thecocktaildb.di.Injectable
import com.test.thecocktaildb.ui.base.BaseFragment
import com.test.thecocktaildb.ui.cocktail.adapter.recyclerview.CocktailsAdapter
import com.test.thecocktaildb.ui.cocktail.host.HostFragmentDirections
import com.test.thecocktaildb.ui.cocktail.host.SharedHostViewModel
import com.test.thecocktaildb.util.EventObserver

class FavoriteFragment : BaseFragment<FavoriteFragmentBinding, FavoriteViewModel>(), Injectable {

    companion object {
        fun newInstance() = FavoriteFragment()
    }

    override val layoutId: Int = R.layout.favorite_fragment

    override fun getViewModelClass(): Class<FavoriteViewModel> = FavoriteViewModel::class.java

    private val sharedHostViewModel: SharedHostViewModel by activityViewModels{delegatedViewModelFactory}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        attachBindingVariable()
        setupNavigation()

        setupRecyclerView()

        return viewDataBinding.root
    }

    private fun attachBindingVariable() {
        viewDataBinding.sharedViewModel = sharedHostViewModel
    }

    private fun setupNavigation() {
        sharedHostViewModel.cocktailDetailsEvent?.observe(
            viewLifecycleOwner,
            EventObserver {
                val (actionBarTitle, cocktailId) = it
                val action = HostFragmentDirections
                    .actionHostFragmentToCocktailDetailsFragment(actionBarTitle, cocktailId)
                findNavController().navigate(action)
            })
    }

    private fun setupRecyclerView() {
        val cocktailsAdapter = CocktailsAdapter(sharedHostViewModel)
        viewDataBinding.cocktailsFavoriteRv.apply {
            adapter = cocktailsAdapter
            layoutManager = GridLayoutManager(activity, 2)
        }
    }
}
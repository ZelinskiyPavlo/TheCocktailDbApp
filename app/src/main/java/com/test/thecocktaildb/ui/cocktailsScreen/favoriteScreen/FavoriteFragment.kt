package com.test.thecocktaildb.ui.cocktailsScreen.favoriteScreen

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.test.thecocktaildb.R
import com.test.thecocktaildb.data.Cocktail
import com.test.thecocktaildb.databinding.FavoriteFragmentBinding
import com.test.thecocktaildb.di.Injectable
import com.test.thecocktaildb.ui.base.BaseFragment
import com.test.thecocktaildb.ui.cocktailsScreen.CocktailsAdapter
import com.test.thecocktaildb.ui.cocktailsScreen.callback.FragmentEventCallback
import com.test.thecocktaildb.ui.cocktailsScreen.callback.OnFavoriteClicked
import com.test.thecocktaildb.ui.cocktailsScreen.callback.OnFilterApplied
import com.test.thecocktaildb.ui.cocktailsScreen.drinkFilter.DrinkFilter
import com.test.thecocktaildb.ui.cocktailsScreen.fragmentHostScreen.HostFragmentDirections
import com.test.thecocktaildb.util.EventObserver

class FavoriteFragment : BaseFragment<FavoriteFragmentBinding, FavoriteViewModel>(), Injectable,
    OnFilterApplied {

    companion object {
        fun newInstance() = FavoriteFragment()
    }

    override val layoutId: Int = R.layout.favorite_fragment

    override fun getViewModelClass(): Class<FavoriteViewModel> = FavoriteViewModel::class.java

    private lateinit var onFavoriteClickedCallback: OnFavoriteClicked

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity() as FragmentEventCallback).addCallback(this)
        onFavoriteClickedCallback = context as OnFavoriteClicked
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        attachBindingVariable()
        setupNavigation()

        setupRecyclerView()

        loadFavoriteCocktail()
        return viewDataBinding.root
    }

    private fun attachBindingVariable() {
        viewDataBinding.viewModel = this.viewModel
    }

    private fun setupNavigation() {
        viewDataBinding.viewModel?.cocktailDetailsEvent?.observe(
            viewLifecycleOwner,
            EventObserver {
                val (actionBarTitle, cocktailId) = it
                val action = HostFragmentDirections
                    .actionHostFragmentToCocktailDetailsFragment(actionBarTitle, cocktailId)
                findNavController().navigate(action)
            })
    }

    private fun setupRecyclerView() {
        val cocktailsAdapter = CocktailsAdapter(viewModel)
        viewDataBinding.cocktailsFavoriteRv.apply {
            adapter = cocktailsAdapter
            layoutManager = GridLayoutManager(activity, 2)
        }
    }

    private fun loadFavoriteCocktail() {
        viewDataBinding.viewModel?.loadFavoriteCocktails()
    }

    override fun onDetach() {
        super.onDetach()
        (requireActivity() as FragmentEventCallback).removeCallback(this)
    }

    override fun applyFilter(filterTypeList: List<DrinkFilter?>) {
        viewDataBinding.viewModel?.applyFilter(filterTypeList)
    }

    override fun resetFilter() {
        viewDataBinding.viewModel?.applyFilter(listOf(null))
    }

    fun updateFavoriteCocktail(cocktail: Cocktail) {
        viewDataBinding.viewModel?.updateFavoriteList(cocktail)
    }
}
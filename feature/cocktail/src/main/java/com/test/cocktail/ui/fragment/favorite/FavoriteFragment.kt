package com.test.cocktail.ui.fragment.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.test.cocktail.R
import com.test.cocktail.api.CocktailNavigationApi
import com.test.cocktail.databinding.FragmentFavoriteBinding
import com.test.cocktail.factory.CocktailViewModelFactory
import com.test.cocktail.ui.CocktailViewModel
import com.test.cocktail.ui.adapter.recyclerview.CocktailAdapter
import com.test.presentation.factory.SavedStateViewModelFactory
import com.test.presentation.ui.base.BaseFragment
import com.test.presentation.util.EventObserver
import javax.inject.Inject

class FavoriteFragment : BaseFragment<FragmentFavoriteBinding>() {

    companion object {
        @JvmStatic
        fun newInstance(): FavoriteFragment {
            return FavoriteFragment()
        }
    }

    override val layoutId: Int = R.layout.fragment_favorite

    @Inject
    lateinit var cocktailViewModelFactory: CocktailViewModelFactory

    private val cocktailViewModel: CocktailViewModel by viewModels({ requireParentFragment() }) {
        SavedStateViewModelFactory(cocktailViewModelFactory, requireParentFragment())
    }

    @Inject
    lateinit var cocktailNavigator: CocktailNavigationApi

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        setupNavigation()
        setupRecyclerView()

        return viewDataBinding.root
    }

    override fun configureDataBinding() {
        super.configureDataBinding()
        viewDataBinding.sharedViewModel = cocktailViewModel
    }

    private fun setupNavigation() {
        cocktailViewModel.cocktailDetailsEventLiveData.observe(
            viewLifecycleOwner,
            EventObserver {
                val (actionBarTitle, cocktailId) = it
                cocktailNavigator.toCocktailDetail(actionBarTitle, cocktailId)
            })
    }

    private fun setupRecyclerView() {
        val adapter = CocktailAdapter(cocktailViewModel).asFavorite
        val recyclerView = viewDataBinding.cocktailsFavoriteRv
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(
            DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL).apply {
//                setDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.item_cocktail_favorite_divider)!!)
            }
        )
        recyclerView.layoutManager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.VERTICAL, false
        )

        cocktailViewModel.favoriteCocktailsLiveData.observe(viewLifecycleOwner, { cocktails ->
            adapter.setData(cocktails, cocktailViewModel.sortingOrderLiveData.value)
        })
        cocktailViewModel.sortingOrderLiveData.observe(viewLifecycleOwner, {sortType ->
            adapter.setData(cocktailViewModel.cocktailsLiveData.value, sortType)
        })
    }
}
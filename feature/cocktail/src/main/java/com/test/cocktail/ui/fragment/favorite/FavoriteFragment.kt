package com.test.cocktail.ui.fragment.favorite

import android.os.Bundle
import android.os.Parcelable
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
    internal lateinit var cocktailViewModelFactory: CocktailViewModelFactory

    private val cocktailViewModel: CocktailViewModel by viewModels({ requireParentFragment() }) {
        SavedStateViewModelFactory(cocktailViewModelFactory, requireParentFragment())
    }

    @Inject
    lateinit var cocktailNavigator: CocktailNavigationApi

    private var recyclerViewParcelable: Parcelable? = null

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
            EventObserver { cocktailId ->
                cocktailNavigator.toCocktailDetail(cocktailId)
            })
    }

    private fun setupRecyclerView() {
        val adapter = CocktailAdapter(cocktailViewModel).asFavorite
        val recyclerView = viewDataBinding.cocktailsFavoriteRv
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(
            DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL)
        )
        recyclerView.layoutManager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.VERTICAL, false
        )

        cocktailViewModel.favoriteCocktailsLiveData.observe(viewLifecycleOwner, { cocktails ->
            saveRecyclerViewState()
            adapter.setData(cocktails, cocktailViewModel.sortingOrderLiveData.value)
            restoreRecyclerViewState()
        })
        cocktailViewModel.sortingOrderLiveData.observe(viewLifecycleOwner, { sortType ->
            saveRecyclerViewState()
            adapter.setData(cocktailViewModel.favoriteCocktailsLiveData.value, sortType)
            restoreRecyclerViewState()
        })
    }

    private fun saveRecyclerViewState() {
        recyclerViewParcelable = viewDataBinding.cocktailsFavoriteRv.layoutManager
            ?.onSaveInstanceState()
    }

    private fun restoreRecyclerViewState() {
        viewDataBinding.cocktailsFavoriteRv.layoutManager
            ?.onRestoreInstanceState(recyclerViewParcelable)
    }
}
package com.test.cocktail.ui.fragment.favorite

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
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
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
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

    private val favoriteAdapter by lazy { CocktailAdapter(cocktailViewModel).asFavorite }

    private var recyclerViewParcelable: Parcelable? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        setupRecyclerView()

        return viewDataBinding.root
    }

    override fun configureDataBinding() {
        super.configureDataBinding()
        viewDataBinding.sharedViewModel = cocktailViewModel
    }

    override fun setupObservers() {
        super.setupObservers()
        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                cocktailViewModel.eventsFlow.onEach { event ->
                    when (event) {
                        // Temporary didn't make navigation bc it's handled in HistoryFragment
//                        is CocktailViewModel.Event.ToDetails ->
//                            cocktailNavigator.toCocktailDetail(event.cocktailId)

                        else -> Unit
                    }
                }.launchIn(this)

                cocktailViewModel.favoriteCocktailsFlow.onEach { cocktails ->
                    saveRecyclerViewState()
                    favoriteAdapter.setData(cocktails, cocktailViewModel.sortingOrderFlow.value)
                    restoreRecyclerViewState()
                }.launchIn(this)

                cocktailViewModel.sortingOrderFlow.onEach { sortType ->
                    saveRecyclerViewState()
                    favoriteAdapter.setData(cocktailViewModel.favoriteCocktailsFlow.value, sortType)
                    restoreRecyclerViewState()
                }.launchIn(this)
            }
        }
    }

    private fun setupRecyclerView() {
        with(viewDataBinding.cocktailsFavoriteRv) {
            adapter = favoriteAdapter
            addItemDecoration(
                DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL)
            )
            layoutManager = LinearLayoutManager(
                requireContext(), LinearLayoutManager.VERTICAL, false
            )
        }
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
package com.test.cocktail.ui.fragment.history

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.test.cocktail.R
import com.test.cocktail.api.CocktailNavigationApi
import com.test.cocktail.databinding.FragmentHistoryBinding
import com.test.cocktail.factory.CocktailViewModelFactory
import com.test.cocktail.ui.CocktailViewModel
import com.test.cocktail.ui.adapter.recyclerview.CocktailAdapter
import com.test.cocktail.ui.adapter.recyclerview.CocktailItemDecoration
import com.test.cocktail.ui.adapter.recyclerview.CocktailLayoutManager
import com.test.presentation.factory.SavedStateViewModelFactory
import com.test.presentation.ui.base.BaseFragment
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

class HistoryFragment : BaseFragment<FragmentHistoryBinding>() {

    companion object {
        @JvmStatic
        fun newInstance(): HistoryFragment {
            return HistoryFragment()
        }
    }

    override val layoutId: Int = R.layout.fragment_history

    @Inject
    internal lateinit var cocktailViewModelFactory: CocktailViewModelFactory

    private val cocktailViewModel: CocktailViewModel by viewModels({ requireParentFragment() }) {
        SavedStateViewModelFactory(cocktailViewModelFactory, requireParentFragment())
    }

    @Inject
    lateinit var cocktailNavigator: CocktailNavigationApi

    private val historyAdapter by lazy { CocktailAdapter(cocktailViewModel) }

    private var recyclerViewParcelable: Parcelable? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
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
                        is CocktailViewModel.Event.ToDetails ->
                            cocktailNavigator.toCocktailDetail(event.cocktailId)

                        else -> Unit
                    }
                }.launchIn(this)

                cocktailViewModel.cocktailsFlow.onEach { cocktails ->
                    saveRecyclerViewState()
                    historyAdapter.setData(cocktails, cocktailViewModel.sortingOrderFlow.value)
                    restoreRecyclerViewState()
                }.launchIn(this)

                cocktailViewModel.sortingOrderFlow.onEach { sortType ->
                    saveRecyclerViewState()
                    historyAdapter.setData(cocktailViewModel.cocktailsFlow.value, sortType)
                    restoreRecyclerViewState()
                }.launchIn(this)
            }
        }
    }

    private fun setupRecyclerView() {
        with(viewDataBinding.cocktailsRv) {
            adapter = historyAdapter
            addItemDecoration(
                CocktailItemDecoration(
                    context = requireContext(),
                    horizontalDpOffSet = R.dimen.margin_8dp,
                    verticalDpOffSet = R.dimen.margin_8dp
                )
            )
            layoutManager = CocktailLayoutManager(requireContext(), 2, historyAdapter)
        }
    }

    private fun saveRecyclerViewState() {
        recyclerViewParcelable = viewDataBinding.cocktailsRv.layoutManager
            ?.onSaveInstanceState()
    }

    private fun restoreRecyclerViewState() {
        viewDataBinding.cocktailsRv.layoutManager
            ?.onRestoreInstanceState(recyclerViewParcelable)
    }
}
package com.test.cocktail.ui.fragment.history

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
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
import com.test.presentation.util.EventObserver
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

    private var recyclerViewParcelable: Parcelable? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
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
            EventObserver {cocktailId ->
                cocktailNavigator.toCocktailDetail(cocktailId)
            })
    }

    private fun setupRecyclerView() {
        val adapter = CocktailAdapter(cocktailViewModel)
        val recyclerView = viewDataBinding.cocktailsRv
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(
            CocktailItemDecoration(
                context = requireContext(),
                horizontalDpOffSet = R.dimen.margin_8dp,
                verticalDpOffSet = R.dimen.margin_8dp
            )
        )
        recyclerView.layoutManager = CocktailLayoutManager(requireContext(), 2, adapter)

        cocktailViewModel.cocktailsLiveData.observe(viewLifecycleOwner, { cocktails ->
            saveRecyclerViewState()
            adapter.setData(cocktails, cocktailViewModel.sortingOrderLiveData.value)
            restoreRecyclerViewState()
        })
        cocktailViewModel.sortingOrderLiveData.observe(viewLifecycleOwner, { sortType ->
            saveRecyclerViewState()
            adapter.setData(cocktailViewModel.cocktailsLiveData.value, sortType)
            restoreRecyclerViewState()
        })
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
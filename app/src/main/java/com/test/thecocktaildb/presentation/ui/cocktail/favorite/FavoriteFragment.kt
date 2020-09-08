package com.test.thecocktaildb.presentation.ui.cocktail.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.test.thecocktaildb.R
import com.test.thecocktaildb.databinding.FragmentFavoriteBinding
import com.test.thecocktaildb.di.Injectable
import com.test.thecocktaildb.presentation.ui.base.BaseFragment
import com.test.thecocktaildb.presentation.ui.cocktail.adapter.recyclerview.CocktailAdapter
import com.test.thecocktaildb.presentation.ui.cocktail.host.HostFragmentDirections
import com.test.thecocktaildb.presentation.ui.cocktail.host.SharedHostViewModel
import com.test.thecocktaildb.util.EventObserver
import com.test.thecocktaildb.util.SavedStateViewModelFactory
import com.test.thecocktaildb.util.SharedHostViewModelFactory
import javax.inject.Inject

class FavoriteFragment : BaseFragment<FragmentFavoriteBinding>(), Injectable {

    companion object {
        @JvmStatic
        fun newInstance() = FavoriteFragment()
    }

    override val layoutId: Int = R.layout.fragment_favorite

    @Inject
    lateinit var sharedHostViewModelFactory: SharedHostViewModelFactory

    private val sharedHostViewModel: SharedHostViewModel by activityViewModels {
        SavedStateViewModelFactory(sharedHostViewModelFactory, requireActivity(), null)
    }

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
        viewDataBinding.sharedViewModel = sharedHostViewModel
    }

    private fun setupNavigation() {
        sharedHostViewModel.cocktailDetailsEventLiveData.observe(
            viewLifecycleOwner,
            EventObserver {
                val (actionBarTitle, cocktailId) = it
                val action = HostFragmentDirections
                    .actionHostFragmentToCocktailDetailsFragment(actionBarTitle, cocktailId.toLong())
                findNavController().navigate(action)
            })
    }

    private fun setupRecyclerView() {
        val adapter = CocktailAdapter(sharedHostViewModel).asFavorite
        val recyclerView = viewDataBinding.cocktailsFavoriteRv
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(
            DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL).apply {
                setDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.item_cocktail_favorite_divider)!!)
            }
        )

        recyclerView.layoutManager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.VERTICAL, false
        )
    }
}
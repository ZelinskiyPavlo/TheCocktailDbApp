package com.test.thecocktaildb.ui.cocktailsScreen

import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.test.thecocktaildb.R
import com.test.thecocktaildb.databinding.CocktailsFragmentBinding
import com.test.thecocktaildb.di.Injectable
import com.test.thecocktaildb.ui.base.BaseFragment
import com.test.thecocktaildb.ui.cocktailsScreen.callback.DrinkProposalCallback
import com.test.thecocktaildb.ui.cocktailsScreen.callback.FragmentEventCallback
import com.test.thecocktaildb.ui.cocktailsScreen.callback.OnFavoriteClicked
import com.test.thecocktaildb.ui.cocktailsScreen.callback.OnFilterApplied
import com.test.thecocktaildb.ui.cocktailsScreen.drinkFilter.DrinkFilter
import com.test.thecocktaildb.ui.cocktailsScreen.fragmentHostScreen.HostFragmentDirections
import com.test.thecocktaildb.ui.cocktailsScreen.sortType.CocktailSortType
import com.test.thecocktaildb.util.EventObserver
import com.test.thecocktaildb.util.receiver.DrinkProposalReceiver
import com.test.thecocktaildb.util.service.ACTION_PROPOSE_DRINK

class CocktailsFragment : BaseFragment<CocktailsFragmentBinding, CocktailsViewModel>(), Injectable,
    DrinkProposalCallback, OnFilterApplied {

    companion object {
        fun newInstance(): CocktailsFragment {
            return CocktailsFragment()
        }
    }

    override val layoutId: Int = R.layout.cocktails_fragment

    override fun getViewModelClass() = CocktailsViewModel::class.java

    private lateinit var drinkProposalReceiver: BroadcastReceiver

    private lateinit var onFavoriteClickedCallback: OnFavoriteClicked

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity() as FragmentEventCallback).addCallback(this)
        onFavoriteClickedCallback = context as OnFavoriteClicked
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        attachBindingVariable()
        initNavigation()

        setupNavigation()
        setupRecyclerView()
        loadCocktails()
        addFavoriteCallback()

        return viewDataBinding.root
    }

    private fun attachBindingVariable() {
        viewDataBinding.viewModel = viewModel
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
        viewDataBinding.cocktailsRv.apply {
            adapter = cocktailsAdapter
            layoutManager = GridLayoutManager(activity, 2)
        }
    }

    private fun loadCocktails() {
        viewModel.loadCocktails()
    }

    private fun addFavoriteCallback() {
        viewDataBinding.viewModel?.favoriteAddedEvent?.observe(viewLifecycleOwner, EventObserver{
            onFavoriteClickedCallback.onFavoriteAdded(it)
        })
    }

    override fun onStart() {
        super.onStart()

        registerDrinkProposalReceiver()
    }

    private fun registerDrinkProposalReceiver() {
        drinkProposalReceiver = DrinkProposalReceiver(this)

        val intentFilter = IntentFilter().apply {
            addAction(ACTION_PROPOSE_DRINK)
        }
        activity?.registerReceiver(drinkProposalReceiver, intentFilter)
    }

    override fun onStop() {
        super.onStop()

        activity?.unregisterReceiver(drinkProposalReceiver)
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

    override fun applySorting(cocktailSortType: CocktailSortType?) {
        viewDataBinding.viewModel?.applySorting(cocktailSortType)
    }

    override fun proposeCocktail(selectedCocktailId: String) {
        if ((viewDataBinding.viewModel?.items?.value?.size ?: 0) > 1) {
            val proposalSnackbar = Snackbar.make(
                viewDataBinding.root,
                getString(R.string.proposal_snackbar_message),
                Snackbar.LENGTH_LONG
            )
            proposalSnackbar.setAction(getString(R.string.show_proposed_cocktail)) {
                viewDataBinding.viewModel?.openProposedCocktail(selectedCocktailId)
            }
            proposalSnackbar.show()
        }
    }
}
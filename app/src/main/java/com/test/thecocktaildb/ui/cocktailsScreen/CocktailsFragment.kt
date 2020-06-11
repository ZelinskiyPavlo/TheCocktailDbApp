package com.test.thecocktaildb.ui.cocktailsScreen

import android.content.BroadcastReceiver
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.test.thecocktaildb.R
import com.test.thecocktaildb.databinding.CocktailsFragmentBinding
import com.test.thecocktaildb.di.Injectable
import com.test.thecocktaildb.ui.base.BaseFragment
import com.test.thecocktaildb.util.EventObserver
import com.test.thecocktaildb.util.receiver.DrinkProposalReceiver

class CocktailsFragment : Injectable, BaseFragment<CocktailsFragmentBinding, CocktailsViewModel>() {

    private lateinit var drinkProposalReceiver: BroadcastReceiver

    override fun getLayoutId(): Int = R.layout.cocktails_fragment

    override fun getViewModelClass() = CocktailsViewModel::class.java

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        attachBindingVariable()

        setupFab()
        setupNavigation()
        setupRecyclerView()
        loadCocktails()

        return mViewDataBinding.root
    }

    private fun attachBindingVariable() {
        mViewDataBinding.viewModel = mViewModel
    }

    private fun setupFab() {
        mViewDataBinding.searchFab.setOnClickListener {
            val action = CocktailsFragmentDirections
                .actionCocktailsFragmentToSearchCocktailsFragment()
            findNavController().navigate(action)
        }
    }

    private fun setupNavigation() {
        mViewDataBinding.viewModel?.cocktailDetailsEvent?.observe(
            viewLifecycleOwner,
            EventObserver {
                val (actionBarTitle, cocktailId) = it
                val action = CocktailsFragmentDirections
                    .actionCocktailsFragmentToCocktailDetailsFragment(actionBarTitle, cocktailId)
                findNavController().navigate(action)
            })
    }

    private fun setupRecyclerView() {
        val cocktailsAdapter = CocktailsAdapter(mViewModel)
        mViewDataBinding.cocktailsRv.apply {
            adapter = cocktailsAdapter
            layoutManager = GridLayoutManager(activity, 2)
        }
    }

    private fun loadCocktails() {
        mViewModel.loadCocktails()
    }

    override fun onStart() {
        super.onStart()

        drinkProposalReceiver =
            DrinkProposalReceiver()
        val intentFilter = IntentFilter().apply {
            addAction("TEST_ACTION")
        }
        activity?.registerReceiver(drinkProposalReceiver, intentFilter)
    }

    override fun onStop() {
        super.onStop()

        activity?.unregisterReceiver(drinkProposalReceiver)
    }

    private fun showProposalSnackbar(selectedCocktailId: Int) {
        val proposalSnackbar = Snackbar.make(
            mViewDataBinding.root, "Чи хочете ви переглянути ще " +
                    getString(R.string.proposal_snackbar_message), Snackbar.LENGTH_LONG
        )
//        TODO: extract string resource
        proposalSnackbar.setAction("Показати") {
            mViewDataBinding.viewModel.openProposedCocktail(selectedCocktailId)
        }
    }
}
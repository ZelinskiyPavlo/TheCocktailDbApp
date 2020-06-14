package com.test.thecocktaildb.ui.cocktailsScreen

import android.content.BroadcastReceiver
import android.content.Intent
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
import com.test.thecocktaildb.util.BatteryStateHolder
import com.test.thecocktaildb.util.EventObserver
import com.test.thecocktaildb.util.receiver.BatteryStateReceiver
import com.test.thecocktaildb.util.receiver.DrinkProposalReceiver
import com.test.thecocktaildb.util.service.ACTION_PROPOSE_DRINK

class CocktailsFragment : BaseFragment<CocktailsFragmentBinding, CocktailsViewModel>(), Injectable,
    DrinkProposalCallback, BatteryStateCallback {

    private lateinit var drinkProposalReceiver: BroadcastReceiver
    private lateinit var batteryStateReceiver: BroadcastReceiver

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

        registerDrinkProposalReceiver()
        registerBatteryStatusReceiver()
    }

    private fun registerDrinkProposalReceiver() {
        drinkProposalReceiver = DrinkProposalReceiver(this)

        val intentFilter = IntentFilter().apply {
            addAction(ACTION_PROPOSE_DRINK)
        }
        activity?.registerReceiver(drinkProposalReceiver, intentFilter)
    }

    private fun registerBatteryStatusReceiver() {
        batteryStateReceiver = BatteryStateReceiver(this)

        val intentFilter = IntentFilter().apply {
            addAction(Intent.ACTION_BATTERY_CHANGED)
            addAction(Intent.ACTION_BATTERY_OKAY)
            addAction(Intent.ACTION_BATTERY_LOW)
        }
        activity?.registerReceiver(batteryStateReceiver, intentFilter)
    }

    override fun onStop() {
        super.onStop()

        activity?.unregisterReceiver(drinkProposalReceiver)
        activity?.unregisterReceiver(batteryStateReceiver)
    }

    override fun proposeCocktail(selectedCocktailId: String) {
        // show proposal snackbar only if there are at least 2 cocktail in history
        if ((mViewDataBinding.viewModel?.items?.value?.size ?: 0) > 1) {
            val proposalSnackbar = Snackbar.make(
                mViewDataBinding.root,
                getString(R.string.proposal_snackbar_message),
                Snackbar.LENGTH_LONG
            )
            proposalSnackbar.setAction(getString(R.string.show_proposed_cocktail)) {
                mViewDataBinding.viewModel?.openProposedCocktail(selectedCocktailId)
            }
            proposalSnackbar.show()
        }
    }

    override fun updateBatteryState(batteryState: BatteryStateHolder) {
        mViewDataBinding.viewModel?.updateBatteryState(batteryState)
    }
}
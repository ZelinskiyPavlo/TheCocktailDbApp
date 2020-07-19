package com.test.thecocktaildb.ui.cocktailScreen.historyScreen

import android.content.BroadcastReceiver
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.test.thecocktaildb.R
import com.test.thecocktaildb.databinding.FragmentHistoryBinding
import com.test.thecocktaildb.di.Injectable
import com.test.thecocktaildb.ui.base.BaseFragment
import com.test.thecocktaildb.ui.cocktailScreen.adapter.CocktailAdapter
import com.test.thecocktaildb.ui.cocktailScreen.callback.DrinkProposalCallback
import com.test.thecocktaildb.ui.cocktailScreen.fragmentHostScreen.HostFragmentDirections
import com.test.thecocktaildb.ui.cocktailScreen.fragmentHostScreen.SharedHostViewModel
import com.test.thecocktaildb.util.EventObserver
import com.test.thecocktaildb.util.SavedStateViewModelFactory
import com.test.thecocktaildb.util.SharedHostViewModelFactory
import com.test.thecocktaildb.util.receiver.DrinkProposalReceiver
import com.test.thecocktaildb.util.service.ACTION_PROPOSE_DRINK
import javax.inject.Inject

class HistoryFragment : BaseFragment<FragmentHistoryBinding>(), Injectable,
    DrinkProposalCallback {

    companion object {
        @JvmStatic
        fun newInstance(): HistoryFragment {
            return HistoryFragment()
        }
    }

    override val layoutId: Int = R.layout.fragment_history

    @Inject
    lateinit var sharedHostViewModelFactory: SharedHostViewModelFactory

    private val sharedHostViewModel: SharedHostViewModel by activityViewModels {
        SavedStateViewModelFactory(sharedHostViewModelFactory, requireActivity(), null)
    }

    private lateinit var drinkProposalReceiver: BroadcastReceiver

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
        viewDataBinding.sharedViewModel = sharedHostViewModel
    }

    private fun setupNavigation() {
        sharedHostViewModel.cocktailDetailsEventLiveData.observe(
            viewLifecycleOwner,
            EventObserver {
                val (actionBarTitle, cocktailId) = it
                val action = HostFragmentDirections
                    .actionHostFragmentToCocktailDetailsFragment(
                        actionBarTitle, cocktailId
                    )
                findNavController().navigate(action)
            })
    }

    private fun setupRecyclerView() {
        val cocktailsAdapter =
            CocktailAdapter(
                sharedHostViewModel
            )
        viewDataBinding.cocktailsRv.apply {
            adapter = cocktailsAdapter
            layoutManager = GridLayoutManager(activity, 2)
        }
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

    override fun proposeCocktail(selectedCocktailId: Long) {
        if ((sharedHostViewModel.cocktailsLiveData.value?.size ?: 0) > 1
            && selectedCocktailId != -1L
        ) {
            val proposalSnackbar = Snackbar.make(
                viewDataBinding.root,
                getString(R.string.proposal_snackbar_message),
                Snackbar.LENGTH_LONG
            )
            proposalSnackbar.setAction(getString(R.string.show_proposed_cocktail)) {
                sharedHostViewModel.openProposedCocktail(selectedCocktailId)
            }
            proposalSnackbar.show()
        }
    }
}
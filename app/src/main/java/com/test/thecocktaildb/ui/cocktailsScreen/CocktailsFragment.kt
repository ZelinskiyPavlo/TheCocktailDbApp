package com.test.thecocktaildb.ui.cocktailsScreen

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.*
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.test.thecocktaildb.R
import com.test.thecocktaildb.databinding.CocktailsFragmentBinding
import com.test.thecocktaildb.di.Injectable
import com.test.thecocktaildb.ui.base.BaseFragment
import com.test.thecocktaildb.ui.cocktailsScreen.drinkFilter.DrinkFilter
import com.test.thecocktaildb.util.BatteryStateHolder
import com.test.thecocktaildb.util.EventObserver
import com.test.thecocktaildb.util.receiver.BatteryStateReceiver
import com.test.thecocktaildb.util.receiver.DrinkProposalReceiver
import com.test.thecocktaildb.util.service.ACTION_PROPOSE_DRINK
import timber.log.Timber

class CocktailsFragment : BaseFragment<CocktailsFragmentBinding, CocktailsViewModel>(), Injectable,
    DrinkProposalCallback, BatteryStateCallback {

    companion object {
        fun newInstance(filterTypeIndex: Int): CocktailsFragment {
            val cocktailsFragment = CocktailsFragment()
            val args = Bundle()
            args.putInt("FILTER_TYPE", filterTypeIndex)
            cocktailsFragment.arguments = args
            return cocktailsFragment
        }
    }

    override val layoutId: Int = R.layout.cocktails_fragment

    override fun getViewModelClass() = CocktailsViewModel::class.java

    private lateinit var drinkProposalReceiver: BroadcastReceiver
    private lateinit var batteryStateReceiver: BroadcastReceiver

    private lateinit var fragmentNavigationListener: FragmentNavigationListener

    private var isFilterApplied: Boolean = false

    override fun onAttach(context: Context) {
        super.onAttach(context)
        fragmentNavigationListener = context as FragmentNavigationListener
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
        setupFab()
        setupRecyclerView()
        loadCocktails()

        setHasOptionsMenu(true)
        return viewDataBinding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.cocktail_fragment_menu, menu)

        val menuItem = menu.findItem(R.id.menu_filter)
        val imageButton = ImageButton(activity)
        imageButton.setImageResource(R.drawable.ic_filter_list_24)
        imageButton.background = null
        menuItem.actionView = imageButton
        menuItem.actionView.setOnLongClickListener {
            applyFilter(listOf(null))
            true
        }
        menuItem.actionView.setOnClickListener { menu.performIdentifierAction(R.id.menu_filter, 0) }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)

        val filterMenuItem: MenuItem = menu.findItem(R.id.menu_filter)
        Timber.i("onPrepareOptionsMenu called $isFilterApplied")
        if (isFilterApplied) {
            filterMenuItem.icon = ContextCompat
                .getDrawable(requireActivity(), R.drawable.ic_filter_list_24_indicator)
        } else {
            filterMenuItem.icon = ContextCompat
                .getDrawable(requireActivity(), R.drawable.ic_filter_list_24)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_filter -> {
                fragmentNavigationListener.navigateToFilterFragment()
                true
            }
            else -> false
        }
    }

    private fun attachBindingVariable() {
        viewDataBinding.viewModel = viewModel
    }

    private fun initNavigation() {
        (activity as AppCompatActivity).setSupportActionBar(viewDataBinding.cocktailToolbar)

        val navController = NavHostFragment.findNavController(this)

        NavigationUI.setupWithNavController(viewDataBinding.cocktailToolbar, navController)
    }

    private fun setupNavigation() {
        viewDataBinding.viewModel?.cocktailDetailsEvent?.observe(
            viewLifecycleOwner,
            EventObserver {
                val (actionBarTitle, cocktailId) = it
                val action = CocktailsFragmentDirections
                    .actionCocktailsFragmentToCocktailDetailsFragment(actionBarTitle, cocktailId)
                findNavController().navigate(action)
            })
    }

    private fun setupFab() {
        viewDataBinding.searchFab.setOnClickListener {
            val action = CocktailsFragmentDirections
                .actionCocktailsFragmentToSearchCocktailsFragment()
            findNavController().navigate(action)
        }
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

    override fun updateBatteryState(batteryState: BatteryStateHolder) {
        viewDataBinding.viewModel?.updateBatteryState(batteryState)
    }

    fun applyFilter(filterTypeList: List<DrinkFilter?>) {
        viewDataBinding.viewModel?.applyFilter(filterTypeList)

        if (filterTypeList.filterNotNull().isEmpty().not())
            changeFilterIndicator(true)
    }

    private fun changeFilterIndicator(filterState: Boolean) {
        isFilterApplied = filterState
        activity?.invalidateOptionsMenu()
    }
}
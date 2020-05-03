package com.test.thecocktaildb.searchCocktailsScreen

import android.os.Bundle
import android.view.*
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.test.thecocktaildb.R
import com.test.thecocktaildb.databinding.SearchCocktailsFragmentBinding
import com.test.thecocktaildb.di.Injectable
import com.test.thecocktaildb.utils.EventObserver
import timber.log.Timber
import javax.inject.Inject

class SearchCocktailsFragment : Injectable, Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var mViewModel: SearchCocktailsViewModel

    private lateinit var viewDataBinding: SearchCocktailsFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        setupViewModel()

        setupDataBinding(inflater, container)
        setupNavigation()
        setupRecyclerView()


        setHasOptionsMenu(true)
        return viewDataBinding.root
    }

    private fun setupViewModel() {
        mViewModel =
            ViewModelProvider(this, viewModelFactory)[SearchCocktailsViewModel::class.java]
    }

    private fun setupDataBinding(inflater: LayoutInflater, container: ViewGroup?) {
        viewDataBinding = SearchCocktailsFragmentBinding.inflate(inflater, container, false)
            .apply {
                viewModel = mViewModel
            }
        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner
    }

    private fun setupNavigation(){
        viewDataBinding.viewModel?.cocktailDetailsEvent?.observe(viewLifecycleOwner, EventObserver{
            val (actionBarTitle, cocktailId) = it
            val action = SearchCocktailsFragmentDirections
                .actionSearchCocktailsFragmentToCocktailDetailsFragment(actionBarTitle, cocktailId)
            findNavController().navigate(action)
        })
    }

    private fun setupRecyclerView(){
        val cocktailsAdapter = SearchCocktailsAdapter(mViewModel)
        viewDataBinding.searchCocktailsRv.apply {
            adapter = cocktailsAdapter
            layoutManager = GridLayoutManager(activity, 2)
        }
        // Is this needed when i have binding adapter
        viewDataBinding.viewModel?.items?.observe(viewLifecycleOwner, Observer {
            Timber.d("SetData in Fragment called")
            cocktailsAdapter.setData(it)
        })

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_cocktails_menu, menu)

        setupSearchView(menu)
    }

    private fun setupSearchView(menu: Menu) {
        val searchItem: MenuItem? = menu.findItem(R.id.search_menu_item)
        val searchView: SearchView = searchItem?.actionView as SearchView

        setSearchViewExpanded(searchView)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(submittedText: String?): Boolean {
                // Maybe perform keyboard closing
                return false
            }

            override fun onQueryTextChange(query: String?): Boolean {
                mViewModel.searchQuerySubject.onNext(query!!)
                return false
            }
        })
        mViewModel.subscribeToSearchSubject()
    }

    private fun setSearchViewExpanded(searchView: SearchView) {
        searchView.isIconifiedByDefault = true
        searchView.isFocusable = true
        searchView.isIconified = false

    }
}
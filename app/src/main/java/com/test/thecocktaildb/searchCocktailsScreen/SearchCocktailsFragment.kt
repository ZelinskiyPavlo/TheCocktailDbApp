package com.test.thecocktaildb.searchCocktailsScreen

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.test.thecocktaildb.R
import com.test.thecocktaildb.databinding.SearchCocktailsFragmentBinding
import com.test.thecocktaildb.di.Injectable
import com.test.thecocktaildb.utils.EventObserver
import kotlinx.android.synthetic.main.activity_main.*
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
        setupSearchField()

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

    private fun setupNavigation() {
        viewDataBinding.viewModel?.cocktailDetailsEvent?.observe(viewLifecycleOwner, EventObserver {
            val (actionBarTitle, cocktailId) = it
            val action = SearchCocktailsFragmentDirections
                .actionSearchCocktailsFragmentToCocktailDetailsFragment(actionBarTitle, cocktailId)
            findNavController().navigate(action)
        })
    }

    private fun setupRecyclerView() {
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

    private fun setupSearchField() {
        val editText = activity?.toolBar?.findViewById<EditText>(R.id.search_field_edit_text)
        editText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}

            override fun beforeTextChanged(query: CharSequence?, p1: Int, p2: Int, p3: Int) {
                Timber.d("beforeTextChanged Printed $query")
                mViewModel.searchQuerySubject.onNext(query.toString())
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })
        mViewModel.subscribeToSearchSubject()
    }
}
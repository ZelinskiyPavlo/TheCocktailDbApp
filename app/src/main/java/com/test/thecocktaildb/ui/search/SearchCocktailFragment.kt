package com.test.thecocktaildb.ui.search

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.test.thecocktaildb.R
import com.test.thecocktaildb.databinding.FragmentSearchCocktailsBinding
import com.test.thecocktaildb.di.Injectable
import com.test.thecocktaildb.ui.base.BaseFragment
import com.test.thecocktaildb.ui.search.adapter.SearchCocktailsAdapter
import com.test.thecocktaildb.util.EventObserver
import com.test.thecocktaildb.util.GenericSavedStateViewModelFactory
import com.test.thecocktaildb.util.SearchCocktailsViewModelFactory
import javax.inject.Inject

class SearchCocktailFragment : Injectable,
    BaseFragment<FragmentSearchCocktailsBinding>() {

    override val layoutId: Int = R.layout.fragment_search_cocktails

    @Inject
    lateinit var searchCocktailsViewModelFactory: SearchCocktailsViewModelFactory

    val viewModel: SearchCocktailViewModel by viewModels {
        GenericSavedStateViewModelFactory(searchCocktailsViewModelFactory, this) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        setupNavigation()
        setupRecyclerView()
        setupSearchField()

        return viewDataBinding.root
    }

    override fun configureDataBinding() {
        super.configureDataBinding()
        viewDataBinding.viewModel = viewModel

    }

    private fun setupNavigation() {
        viewDataBinding.viewModel?.cocktailDetailsEventLiveData?.observe(
            viewLifecycleOwner, EventObserver {
                val (actionBarTitle, cocktailId) = it
                val action = SearchCocktailFragmentDirections
                    .actionSearchCocktailsFragmentToCocktailDetailsFragment(
                        actionBarTitle, cocktailId
                    )
                findNavController().navigate(action)
            })
    }

    private fun setupRecyclerView() {
        val cocktailsAdapter = SearchCocktailsAdapter(viewModel)
        viewDataBinding.searchCocktailsRv.apply {
            adapter = cocktailsAdapter
            layoutManager = GridLayoutManager(activity, 2)
        }
    }

    private fun setupSearchField() {
        fun showKeyboard(editText: EditText?) {
            editText?.post {
                editText.requestFocus()
                val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
            }
        }

        val editText = viewDataBinding.searchFieldLayout
            .findViewById<EditText>(R.id.search_field_edit_text)

        editText?.setText("")

        showKeyboard(editText)

        editText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(query: Editable?) {
                viewModel.searchQuerySubject.onNext(query.toString())
            }

            override fun beforeTextChanged(query: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })
        viewModel.subscribeToSearchSubject()
    }
}
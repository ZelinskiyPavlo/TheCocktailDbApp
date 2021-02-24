package com.test.search.ui

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
import androidx.recyclerview.widget.GridLayoutManager
import com.test.presentation.factory.SavedStateViewModelFactory
import com.test.presentation.ui.base.BaseFragment
import com.test.presentation.util.EventObserver
import com.test.search.R
import com.test.search.adapter.recyclerview.SearchCocktailAdapter
import com.test.search.adapter.recyclerview.SearchCocktailItemDecoration
import com.test.search.api.SearchNavigationApi
import com.test.search.databinding.FragmentSearchCocktailsBinding
import com.test.search.factory.SearchCocktailsViewModelFactory
import javax.inject.Inject

class SearchCocktailFragment : BaseFragment<FragmentSearchCocktailsBinding>() {

    override val layoutId: Int = R.layout.fragment_search_cocktails

    @Inject
    internal lateinit var searchCocktailsViewModelFactory: SearchCocktailsViewModelFactory

    override val viewModel: SearchCocktailViewModel by viewModels {
        SavedStateViewModelFactory(searchCocktailsViewModelFactory, this)
    }

    @Inject
    lateinit var searchNavigator: SearchNavigationApi

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        setupNavigation()
        setupRecyclerView()
        setupVisibilityObserver()
        setupSearchField()

        return viewDataBinding.root
    }

    override fun configureDataBinding() {
        super.configureDataBinding()
        viewDataBinding.viewModel = viewModel
    }

    private fun setupNavigation() {
        viewModel.cocktailDetailsEventLiveData.observe(
            viewLifecycleOwner, EventObserver { cocktailId ->
                searchNavigator.toCocktailDetail(cocktailId)
            })
    }

    private fun setupRecyclerView() {
        val cocktailsAdapter = SearchCocktailAdapter(viewModel)
        viewDataBinding.searchCocktailsRv.apply {
            adapter = cocktailsAdapter
            layoutManager = GridLayoutManager(activity, 2)
        }
        val decoration = SearchCocktailItemDecoration(
            context = requireContext(),
            horizontalDpOffSet = R.dimen.margin_8dp,
            verticalDpOffSet = R.dimen.margin_16dp
        )
        viewDataBinding.searchCocktailsRv.addItemDecoration(decoration)
    }

    private fun setupVisibilityObserver() {
        with(viewDataBinding) {
            this@SearchCocktailFragment.viewModel.isSearchQueryEmptyLiveData.observe(
                viewLifecycleOwner, { isQueryEmpty ->
                    searchQueryEmptyTv.visibility = if (isQueryEmpty) View.VISIBLE else View.GONE

                    searchCocktailsRv.visibility = if (isQueryEmpty) View.GONE else View.VISIBLE
                    searchResultEmptyTv.visibility = View.GONE
                })
            this@SearchCocktailFragment.viewModel.isSearchResultEmptyLiveData.observe(
                viewLifecycleOwner, {isResultEmpty ->
                    if (isResultEmpty) {
                        searchResultEmptyTv.visibility = View.VISIBLE

                        searchQueryEmptyTv.visibility = View.GONE
                        searchCocktailsRv.visibility = View.GONE
                    }
                })
        }
    }

    private fun setupSearchField() {
        fun showKeyboard(editText: EditText?) {
            editText?.post {
                editText.requestFocus()
                val imm =
                    context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
            }
        }

        val editText = viewDataBinding.searchFieldLayout
            .findViewById<EditText>(R.id.search_field_edit_text)

        editText?.setText("")

        showKeyboard(editText)

        editText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(query: Editable?) {
                viewModel.searchQueryLiveData.value = query.toString()
            }

            override fun beforeTextChanged(query: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })
    }
}
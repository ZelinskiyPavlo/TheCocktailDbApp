package com.test.thecocktaildb.searchCocktailsScreen

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.test.thecocktaildb.R
import com.test.thecocktaildb.databinding.SearchCocktailsFragmentBinding
import com.test.thecocktaildb.di.Injectable
import com.test.thecocktaildb.utils.EventObserver
import kotlinx.android.synthetic.main.activity_main.*
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
    }

    private fun setupSearchField() {
        fun showKeyboard(editText: EditText?) {
            if (editText?.requestFocus() == true) {
                val imm =
                    context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
            }
        }

        val editText = activity?.toolBar?.findViewById<EditText>(R.id.search_field_edit_text)
        editText?.setText("")

        showKeyboard(editText)

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
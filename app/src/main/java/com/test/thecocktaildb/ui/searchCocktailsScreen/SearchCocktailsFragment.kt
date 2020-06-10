package com.test.thecocktaildb.ui.searchCocktailsScreen

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.test.thecocktaildb.R
import com.test.thecocktaildb.databinding.SearchCocktailsFragmentBinding
import com.test.thecocktaildb.di.Injectable
import com.test.thecocktaildb.ui.base.BaseFragment
import com.test.thecocktaildb.util.EventObserver
import kotlinx.android.synthetic.main.activity_main.*

class SearchCocktailsFragment : Injectable,
    BaseFragment<SearchCocktailsFragmentBinding, SearchCocktailsViewModel>() {

    override fun getLayoutId(): Int = R.layout.search_cocktails_fragment

    override fun getViewModelClass(): Class<SearchCocktailsViewModel> =
        SearchCocktailsViewModel::class.java

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        attachBindingVariable()

        setupNavigation()
        setupRecyclerView()
        setupSearchField()

        return mViewDataBinding.root
    }


    private fun attachBindingVariable() {
        mViewDataBinding.viewModel = mViewModel
    }


    private fun setupNavigation() {
        mViewDataBinding.viewModel?.cocktailDetailsEvent?.observe(
            viewLifecycleOwner, EventObserver {
                val (actionBarTitle, cocktailId) = it
                val action = SearchCocktailsFragmentDirections
                    .actionSearchCocktailsFragmentToCocktailDetailsFragment(
                        actionBarTitle, cocktailId
                    )
                findNavController().navigate(action)
            })
    }

    private fun setupRecyclerView() {
        val cocktailsAdapter = SearchCocktailsAdapter(mViewModel)
        mViewDataBinding.searchCocktailsRv.apply {
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
            override fun afterTextChanged(query: Editable?) {
                mViewModel.searchQuerySubject.onNext(query.toString())
            }

            override fun beforeTextChanged(query: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })
        mViewModel.subscribeToSearchSubject()
    }
}
package com.test.search.ui

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.test.presentation.factory.SavedStateViewModelFactory
import com.test.presentation.ui.base.BaseFragment
import com.test.search.R
import com.test.search.adapter.recyclerview.SearchCocktailAdapter
import com.test.search.adapter.recyclerview.SearchCocktailItemDecoration
import com.test.search.databinding.FragmentSearchCocktailsBinding
import com.test.search.factory.SearchCocktailsViewModelFactory
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

class SearchCocktailFragment : BaseFragment<FragmentSearchCocktailsBinding>() {

    override val layoutId: Int = R.layout.fragment_search_cocktails

    @Inject
    internal lateinit var searchCocktailsViewModelFactory: SearchCocktailsViewModelFactory

    override val viewModel: SearchCocktailViewModel by viewModels {
        SavedStateViewModelFactory(searchCocktailsViewModelFactory, this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        setupRecyclerView()
        setupSearchField()
        setupKeyboardClosing()

        return viewDataBinding.root
    }

    override fun configureDataBinding() {
        super.configureDataBinding()
        viewDataBinding.viewModel = viewModel
    }

    override fun setupObservers() {
        super.setupObservers()
        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.eventsFlow.onEach { event ->
                }.launchIn(this)
                viewModel.isSearchQueryEmptyFlow.onEach { isQueryEmpty ->
                    with(viewDataBinding) {
                        searchQueryEmptyTv.visibility =
                            if (isQueryEmpty) View.VISIBLE else View.GONE

                        searchCocktailsRv.visibility =
                            if (isQueryEmpty) View.GONE else View.VISIBLE
                        searchResultEmptyTv.visibility = View.GONE
                    }
                }.launchIn(this)
                viewModel.isSearchResultEmptyFlow.onEach { isResultEmpty ->
                    with(viewDataBinding) {
                        if (isResultEmpty) {
                            searchResultEmptyTv.visibility = View.VISIBLE

                            searchQueryEmptyTv.visibility = View.GONE
                            searchCocktailsRv.visibility = View.GONE
                        }
                    }
                }.launchIn(this)
            }
        }
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

    private fun setupSearchField() {
        fun showKeyboard(editText: EditText?) {
            editText?.post {
                editText.requestFocus()
                val imm =
                    context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
            }
        }

        val editText = viewDataBinding.searchFieldLayout.searchFieldEditText

        editText.setText("")

        showKeyboard(editText)

        editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(query: Editable?) {
                viewModel.searchQueryFlow.value = query.toString()
            }

            override fun beforeTextChanged(query: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupKeyboardClosing() {
        viewDataBinding.searchResultLayout.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                if (viewDataBinding.searchFieldLayout.searchFieldEditText.isFocused) {
                    val outRect = Rect()
                    viewDataBinding.searchFieldLayout.searchFieldEditText
                        .getGlobalVisibleRect(outRect)
                    if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                        viewDataBinding.searchFieldLayout.searchFieldEditText.clearFocus()
                        val imm =
                            v.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.hideSoftInputFromWindow(v.windowToken, 0)
                    }
                }
            }
            false
        }
    }
}
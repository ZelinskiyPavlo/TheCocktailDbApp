package com.test.detail.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.analytics.FirebaseAnalytics
import com.test.cocktail_common.service.DrinkProposalService
import com.test.detail.adapter.IngredientAdapter
import com.test.detail.analytic.logOpenCocktailDetail
import com.test.detail.databinding.FragmentCocktailDetailsBinding
import com.test.detail.factory.CocktailDetailsViewModelFactory
import com.test.navigation.api.SimpleNavigatorApi
import com.test.presentation.factory.SavedStateViewModelFactory
import com.test.presentation.ui.base.BaseFragment
import javax.inject.Inject

class CocktailDetailsFragment : BaseFragment<FragmentCocktailDetailsBinding>() {

    companion object {
        @JvmStatic
        fun newInstance(cocktailId: Long): CocktailDetailsFragment {
            val fragment = CocktailDetailsFragment()
            fragment.arguments = bundleOf(
                COCKTAIL_ID_KEY to cocktailId
            )
            return fragment
        }

        private const val COCKTAIL_ID_KEY = "COCKTAIL_ID_KEY"
    }

    override val layoutId: Int = com.test.detail.R.layout.fragment_cocktail_details

    @Inject
    internal lateinit var cocktailDetailsVmFactory: CocktailDetailsViewModelFactory

    override val viewModel by viewModels<CocktailDetailsViewModel> {
        SavedStateViewModelFactory(cocktailDetailsVmFactory, this)
    }

    @Inject
    lateinit var simpleNavigator: SimpleNavigatorApi

    @Inject
    lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        setupIngredientsRecyclerView()
        setCollapsingToolbarListener()
        setupObserver()
        getCocktail()

        logFirebaseEvent()
        return viewDataBinding.root
    }

    override fun configureDataBinding() {
        super.configureDataBinding()
        viewDataBinding.viewModel = viewModel
        viewDataBinding.fragment = this
    }

    private fun setupIngredientsRecyclerView() {
        val ingredientsAdapter = IngredientAdapter()
        viewDataBinding.ingredientsRv.apply {
            adapter = ingredientsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
        viewModel.ingredientsLiveData.observe(viewLifecycleOwner, {ingredients ->
            ingredientsAdapter.setData(ingredients)
        })
    }

    // TODO: 11.02.2021 Feature request (if this possible) make cocktail image drawing behind status bar

    private fun setCollapsingToolbarListener() {
        viewDataBinding.ablDetails.addOnOffsetChangedListener(
            RoundedImageOnOffsetChangedListener(viewDataBinding, requireContext())
        )
    }

    private fun setupObserver() {
        viewModel.isCocktailFoundLiveData.observe(viewLifecycleOwner, {result ->
            if (result == false) simpleNavigator.exit()
        })
    }

    private fun getCocktail() {
        val cocktailId = requireArguments().getLong(COCKTAIL_ID_KEY)
        viewModel.getCocktailById(cocktailId)
    }

    private fun logFirebaseEvent() {
        firebaseAnalytics.logOpenCocktailDetail(viewModel.cocktailId)
    }

    fun onBackButtonClicked() {
        simpleNavigator.exit()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        launchDrinkProposalService()
    }

    private fun launchDrinkProposalService() {
        val stopIntent = Intent(activity, DrinkProposalService::class.java)
        stopIntent.action = DrinkProposalService.ACTION_STOP_SERVICE
        activity?.startService(stopIntent)

        if (viewModel.isCocktailFoundLiveData.value == false) return

        val startIntent = Intent(activity, DrinkProposalService::class.java)
        startIntent.action = DrinkProposalService.ACTION_START_SERVICE
        val selectedCocktailId = viewModel.cocktailId
        startIntent.putExtra(DrinkProposalService.SELECTED_COCKTAIL_ID, selectedCocktailId)
        activity?.startService(startIntent)
    }
}
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

class CocktailDetailsFragment : Injectable,
    BaseFragment<FragmentCocktailDetailsBinding>() {

    override val layoutId: Int = R.layout.fragment_cocktail_details

    @Inject
    lateinit var cocktailDetailsVmFactory: CocktailDetailsViewModelFactory

    override val viewModel by viewModels<CocktailDetailsViewModel> {
        SavedStateViewModelFactory(cocktailDetailsVmFactory, this)
    }

    private val maxImageWidth: Int by lazy { viewDataBinding.cocktailImage.width }
    private val imageContainerHeight: Int by lazy { viewDataBinding.imageContainer.height }
    private val minImageWidth: Float by lazy { resources.displayMetrics.density * 32F }
    private val imageMarginHorizontal: Float by lazy { resources.displayMetrics.density * 56F }
    private val imageMarginVertical: Float by lazy { resources.displayMetrics.density * 16F }
    private val layoutParams: LinearLayout.LayoutParams
            by lazy { viewDataBinding.cocktailImage.layoutParams as LinearLayout.LayoutParams }

    @Inject
    lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        setupNavigation()
        setupIngredientsRecyclerView()
        setActionBarTitle()
        setCollapsingToolbarListener()

        getCocktail()

        logFirebaseEvent()
        return viewDataBinding.root
    }

    override fun configureDataBinding() {
        super.configureDataBinding()
        viewDataBinding.viewModel = viewModel
    }

    private fun setupNavigation() {
        val navController = NavHostFragment.findNavController(this)

        viewDataBinding.viewModel?.onBackPressedEventLiveData?.observe(viewLifecycleOwner,
            EventObserver {
                navController.popBackStack()
            })
    }

    private fun setupIngredientsRecyclerView() {
        val ingredientsAdapter = IngredientAdapter()
        viewDataBinding.ingredientsRv.apply {
            adapter = ingredientsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    private fun setActionBarTitle() {
        val actionBarTitle = getNavigationArgs().second
        viewDataBinding.ctlDetail.title = actionBarTitle
    }

    private fun setCollapsingToolbarListener() {
        var setupFlag = true

        viewDataBinding.ablDetails.addOnOffsetChangedListener(
            AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
                if (setupFlag) {
                    viewDataBinding.imageContainer.layoutParams.height =
                        viewDataBinding.cocktailImage.height
                    cachedImageWidth = maxImageWidth
                    viewDataBinding.imageContainer.requestLayout()

                    setupFlag = false
                }

                val fraction = (-verticalOffset).toFloat() / appBarLayout.totalScrollRange.toFloat()

                val currentImageWidth =
                    lerp(maxImageWidth.toFloat(), minImageWidth, fraction).toInt()
                val currentMarginVertical =
                    lerp(0f, (imageContainerHeight / 2 - imageMarginVertical), fraction).toInt()
                val currentMarginHorizontal =
                    lerp(0f, imageMarginHorizontal, fraction).toInt()

                with(layoutParams) {
                    width = currentImageWidth
                    setMargins(
                        currentMarginHorizontal,
                        currentMarginVertical,
                        currentMarginHorizontal,
                        currentMarginVertical
                    )
                }

                if (layoutParams.width != cachedImageWidth) {
                    cachedImageWidth = currentImageWidth
                    viewDataBinding.cocktailImage.layoutParams = layoutParams
                    viewDataBinding.cocktailImage.cornerRadius = lerp(0.0F, maxRadius, fraction)
                }
            })
    }

    private fun getCocktail() {
        val cocktailId = getNavigationArgs().first
        viewModel.getCocktailById(cocktailId)
    }

    private fun getNavigationArgs(): Pair<Long, String> {
        val args by navArgs<CocktailDetailsFragmentArgs>()

        return Pair(args.cocktailId, args.actionBarTitle)
    }

    private fun logFirebaseEvent() {
        firebaseAnalytics.logEvent(
            Analytic.COCKTAIL_DETAIL_OPEN,
            bundleOf(Analytic.COCKTAIL_DETAIL_OPEN_KEY to viewModel.cocktailId)
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()

        launchDrinkProposalService()
    }

    private fun launchDrinkProposalService() {
        val stopIntent = Intent(activity, DrinkProposalService::class.java)
        stopIntent.action = DrinkProposalService.ACTION_STOP_SERVICE
        activity?.startService(stopIntent)

        val startIntent = Intent(activity, DrinkProposalService::class.java)
        startIntent.action = DrinkProposalService.ACTION_START_SERVICE
        val selectedCocktailId = viewDataBinding.viewModel?.cocktailId
        startIntent.putExtra(DrinkProposalService.SELECTED_COCKTAIL_ID, selectedCocktailId)
        activity?.startService(startIntent)

    }
}
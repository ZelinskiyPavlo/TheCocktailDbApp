package com.test.thecocktaildb.presentation.ui.detail

import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.AppBarLayout
import com.test.thecocktaildb.R
import com.test.thecocktaildb.databinding.FragmentCocktailDetailsBinding
import com.test.thecocktaildb.di.Injectable
import com.test.thecocktaildb.presentation.ui.base.BaseFragment
import com.test.thecocktaildb.presentation.ui.detail.adapter.IngredientAdapter
import com.test.thecocktaildb.util.CocktailDetailsViewModelFactory
import com.test.thecocktaildb.util.EventObserver
import com.test.thecocktaildb.util.SavedStateViewModelFactory
import com.test.thecocktaildb.util.service.DrinkProposalService
import javax.inject.Inject

class CocktailDetailsFragment : Injectable,
    BaseFragment<FragmentCocktailDetailsBinding>() {

    override val layoutId: Int = R.layout.fragment_cocktail_details

    @Inject
    lateinit var cocktailDetailsVmFactory: CocktailDetailsViewModelFactory

    override val viewModel by viewModels<CocktailDetailsViewModel> {
        SavedStateViewModelFactory(cocktailDetailsVmFactory, this)
    }

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
        val displayMetrics = DisplayMetrics()
        activity?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        val maxImageWidth = displayMetrics.widthPixels
        val minImageWidth = (resources.getDimension(R.dimen.cocktail_image_detail_min_width)).toInt()
        var cachedImageWidth = maxImageWidth

        val imageMarginStart = (resources.getDimension(R.dimen.cocktail_image_detail_margin_start)).toInt()
        val imageMarginTop = (resources.getDimension(R.dimen.cocktail_image_detail_margin_top)).toInt()

        val marginParams =
            viewDataBinding.cocktailImage.layoutParams as ViewGroup.MarginLayoutParams

        var setupFlag = true

        viewDataBinding.ablDetails.addOnOffsetChangedListener(
            AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->

                if (setupFlag) {
                    viewDataBinding.imageContainer.layoutParams.height = viewDataBinding
                        .cocktailImage.layoutParams.height
                    viewDataBinding.imageContainer.requestLayout()

                    setupFlag = false
                }

                val totalScrollRange = appBarLayout.totalScrollRange
                val offSetScale = (-verticalOffset).toFloat() / totalScrollRange
                val scaleFactor = 1F - offSetScale

                val currentImageWidth =
                    minImageWidth + ((maxImageWidth - minImageWidth) * scaleFactor)
                viewDataBinding.cocktailImage.layoutParams.width = currentImageWidth.toInt()

                if (viewDataBinding.cocktailImage.layoutParams.width != cachedImageWidth) {
                    cachedImageWidth = currentImageWidth.toInt()
                    viewDataBinding.cocktailImage.requestLayout()
                    viewDataBinding.cocktailImage.layoutParams = marginParams
                }

                marginParams.topMargin =
                    (((maxImageWidth / 2) - imageMarginTop) * offSetScale).toInt()
                marginParams.marginStart = (imageMarginStart * offSetScale).toInt()

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
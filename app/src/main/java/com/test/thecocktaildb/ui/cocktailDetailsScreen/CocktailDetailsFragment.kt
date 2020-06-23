package com.test.thecocktaildb.ui.cocktailDetailsScreen

import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.AppBarLayout
import com.test.thecocktaildb.R
import com.test.thecocktaildb.databinding.CocktailDetailsFragmentBinding
import com.test.thecocktaildb.di.Injectable
import com.test.thecocktaildb.ui.base.BaseFragment
import com.test.thecocktaildb.util.service.DrinkProposalService
import com.test.thecocktaildb.util.EventObserver

class CocktailDetailsFragment : Injectable,
    BaseFragment<CocktailDetailsFragmentBinding, CocktailDetailsViewModel>() {

    override fun getLayoutId(): Int = R.layout.cocktail_details_fragment

    override fun getViewModelClass(): Class<CocktailDetailsViewModel> =
        CocktailDetailsViewModel::class.java

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        attachBindingVariable()
        setupNavigation()
        setupIngredientsRecyclerView()
        setActionBarTitle()

        setCollapsingToolbarListener()

        getCocktail()
        return mViewDataBinding.root
    }

    private fun attachBindingVariable() {
        mViewDataBinding.viewModel = mViewModel
    }

    private fun setupNavigation() {
        val navController = NavHostFragment.findNavController(this)

        mViewDataBinding.viewModel?.onBackPressedEventLiveData?.observe(viewLifecycleOwner,
            EventObserver {
                navController.popBackStack()
            })
    }

    private fun setupIngredientsRecyclerView() {
        val ingredientsAdapter = IngredientsAdapter()
        mViewDataBinding.ingredientsRv.apply {
            adapter = ingredientsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    private fun setActionBarTitle() {
        val actionBarTitle = getNavigationArgs().second
        mViewDataBinding.ctlDetail.title = actionBarTitle
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
            mViewDataBinding.cocktailImage.layoutParams as ViewGroup.MarginLayoutParams

        var setupFlag = true

        mViewDataBinding.ablDetails.addOnOffsetChangedListener(
            AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->

                if (setupFlag) {
                    mViewDataBinding.imageContainer.layoutParams.height = mViewDataBinding
                        .cocktailImage.layoutParams.height
                    mViewDataBinding.imageContainer.requestLayout()

                    setupFlag = false
                }

                val totalScrollRange = appBarLayout.totalScrollRange
                val offSetScale = (-verticalOffset).toFloat() / totalScrollRange
                val scaleFactor = 1F - offSetScale

                val currentImageWidth =
                    minImageWidth + ((maxImageWidth - minImageWidth) * scaleFactor)
                mViewDataBinding.cocktailImage.layoutParams.width = currentImageWidth.toInt()

                if (mViewDataBinding.cocktailImage.layoutParams.width != cachedImageWidth) {
                    cachedImageWidth = currentImageWidth.toInt()
                    mViewDataBinding.cocktailImage.requestLayout()
                    mViewDataBinding.cocktailImage.layoutParams = marginParams
                }

                marginParams.topMargin =
                    (((maxImageWidth / 2) - imageMarginTop) * offSetScale).toInt()
                marginParams.marginStart = (imageMarginStart * offSetScale).toInt()

            })
    }

    private fun getCocktail() {
        val cocktailId = getNavigationArgs().first
        mViewModel.getCocktailById(cocktailId)
    }

    private fun getNavigationArgs(): Pair<String, String> {
        val args by navArgs<CocktailDetailsFragmentArgs>()

        return Pair(args.cocktailId, args.actionBarTitle)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        launchDrinkProposalService()
    }

    private fun launchDrinkProposalService(){
        val intentWithCocktail = Intent(activity, DrinkProposalService::class.java)
        val selectedCocktailId = mViewDataBinding.viewModel?.cocktailId
        intentWithCocktail.putExtra(Intent.EXTRA_TEXT, selectedCocktailId)

        activity?.startService(intentWithCocktail)
    }
}
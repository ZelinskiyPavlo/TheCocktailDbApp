package com.test.cocktail.analytic

import androidx.core.os.bundleOf
import com.google.firebase.analytics.FirebaseAnalytics
import com.test.firebase.common.Analytic
import javax.inject.Inject

class CocktailAnalyticImpl @Inject constructor(private val firebaseAnalytics: FirebaseAnalytics) : CocktailAnalyticApi {

    override fun logCocktailFilterApply(
        selectedFiltersList: List<String>,
        selectedFiltersTypeList: List<String>
    ) {
        if (selectedFiltersList.isNotEmpty()) {
            firebaseAnalytics.logEvent(
                Analytic.COCKTAIL_FILTER_APPLY,
                bundleOf(
                    Analytic.COCKTAIL_FILTER_APPLY_FILTER_TYPE_KEY to selectedFiltersTypeList.joinToString(),
                    Analytic.COCKTAIL_FILTER_APPLY_ALCOHOL_KEY to selectedFiltersList[0],
                    Analytic.COCKTAIL_FILTER_APPLY_CATEGORY_KEY to selectedFiltersList[1],
                    Analytic.COCKTAIL_FILTER_APPLY_INGREDIENT_KEY to selectedFiltersList[2]
                )
            )
        }
    }

    override fun logFavoriteCocktailStateChanged(
        isAddedToFavorite: Boolean,
        cocktailId: String,
        fullUserName: String
    ) {
        val eventName =
            if (isAddedToFavorite) Analytic.COCKTAIL_FAVORITE_ADD
            else Analytic.COCKTAIL_FAVORITE_REMOVE

        firebaseAnalytics.logEvent(
            eventName,
            bundleOf(
                Analytic.COCKTAIL_FAVORITE_ID_KEY to cocktailId,
                Analytic.COCKTAIL_FAVORITE_USER_NAME_KEY to fullUserName
            )
        )
    }

}
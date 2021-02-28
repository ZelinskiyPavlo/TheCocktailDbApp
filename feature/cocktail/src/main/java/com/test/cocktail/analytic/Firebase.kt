package com.test.cocktail.analytic

import androidx.core.os.bundleOf
import com.google.firebase.analytics.FirebaseAnalytics
import com.test.firebase.common.Analytic

internal fun FirebaseAnalytics.logCocktailFilterApply(
    selectedFilters: Pair<List<String>, List<String>>?
) {
    if (selectedFilters != null && selectedFilters.first.isNotEmpty()) {
        val (selectedFiltersList, selectedFiltersTypeList) = selectedFilters
        logEvent(
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

internal fun FirebaseAnalytics.logFavoriteCocktailStateChanged(cocktailStateData: Triple<Boolean, String, String>) {
    val (isAddedToFavorite, cocktailId, fullUserName) = cocktailStateData

    val eventName =
        if (isAddedToFavorite) Analytic.COCKTAIL_FAVORITE_ADD
        else Analytic.COCKTAIL_FAVORITE_REMOVE

    logEvent(
        eventName,
        bundleOf(
            Analytic.COCKTAIL_FAVORITE_ID_KEY to cocktailId,
            Analytic.COCKTAIL_FAVORITE_USER_NAME_KEY to fullUserName
        )
    )
}
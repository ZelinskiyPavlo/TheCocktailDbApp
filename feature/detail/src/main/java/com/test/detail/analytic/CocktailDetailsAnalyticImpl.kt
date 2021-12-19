package com.test.detail.analytic

import androidx.core.os.bundleOf
import com.google.firebase.analytics.FirebaseAnalytics
import com.test.firebase.common.Analytic
import javax.inject.Inject

class CocktailDetailsAnalyticImpl @Inject constructor(private val firebaseAnalytics: FirebaseAnalytics) :
    CocktailDetailsAnalyticApi {

    override fun logOpenCocktailDetail(cocktailId: Long) {
        firebaseAnalytics.logEvent(
            Analytic.COCKTAIL_DETAIL_OPEN,
            bundleOf(Analytic.COCKTAIL_DETAIL_OPEN_KEY to cocktailId)
        )
    }
}
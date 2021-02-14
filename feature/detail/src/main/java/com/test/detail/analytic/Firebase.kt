package com.test.detail.analytic

import androidx.core.os.bundleOf
import com.google.firebase.analytics.FirebaseAnalytics
import com.test.firebase.common.Analytic

internal fun FirebaseAnalytics.logOpenCocktailDetail(cocktailId: Long) {
        logEvent(
            Analytic.COCKTAIL_DETAIL_OPEN,
            bundleOf(Analytic.COCKTAIL_DETAIL_OPEN_KEY to cocktailId)
        )
}

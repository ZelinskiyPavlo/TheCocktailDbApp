package com.test.tabhost.analytic

import androidx.core.os.bundleOf
import com.google.firebase.analytics.FirebaseAnalytics
import com.test.firebase.common.Analytic

internal fun FirebaseAnalytics.logCocktailTabClicked() {
    logEvent(
        Analytic.MAIN_TAB_CHANGE,
        bundleOf(Analytic.MAIN_TAB_CHANGE_KEY to "cocktail")
    )
}

internal fun FirebaseAnalytics.logSettingTabClicked() {
    logEvent(
        Analytic.MAIN_TAB_CHANGE,
        bundleOf(Analytic.MAIN_TAB_CHANGE_KEY to "setting")
    )
}
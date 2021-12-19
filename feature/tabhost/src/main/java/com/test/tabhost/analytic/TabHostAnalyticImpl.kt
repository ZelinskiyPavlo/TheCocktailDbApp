package com.test.tabhost.analytic

import androidx.core.os.bundleOf
import com.google.firebase.analytics.FirebaseAnalytics
import com.test.firebase.common.Analytic
import javax.inject.Inject

class TabHostAnalyticImpl @Inject constructor(private val firebaseAnalytics: FirebaseAnalytics) :
    TabHostAnalyticApi {

    override fun logCocktailTabClicked() {
        firebaseAnalytics.logEvent(
            Analytic.MAIN_TAB_CHANGE,
            bundleOf(Analytic.MAIN_TAB_CHANGE_KEY to "cocktail")
        )
    }

    override fun logSettingTabClicked() {
        firebaseAnalytics.logEvent(
            Analytic.MAIN_TAB_CHANGE,
            bundleOf(Analytic.MAIN_TAB_CHANGE_KEY to "setting")
        )
    }

}
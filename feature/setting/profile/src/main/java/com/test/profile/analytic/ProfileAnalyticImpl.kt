package com.test.profile.analytic

import androidx.core.os.bundleOf
import com.google.firebase.analytics.FirebaseAnalytics
import com.test.firebase.common.Analytic
import javax.inject.Inject

class ProfileAnalyticImpl @Inject constructor(private val firebaseAnalytics: FirebaseAnalytics) :
    ProfileAnalyticApi {

    override fun logUserNameChanged(userFullName: String) {
        firebaseAnalytics.logEvent(
            Analytic.PROFILE_DATA_CHANGE,
            bundleOf(
                Analytic.PROFILE_DATA_CHANGE_KEY to userFullName
            )
        )
    }

    override fun logUserAvatarChanged(newAvatarUrl: String, userFullName: String) {
        firebaseAnalytics.logEvent(
            Analytic.PROFILE_AVATAR_CHANGE,
            bundleOf(
                Analytic.PROFILE_AVATAR_CHANGE_AVATAR_KEY to newAvatarUrl,
                Analytic.PROFILE_AVATAR_CHANGE_NAME_KEY to userFullName
            )
        )
    }

}
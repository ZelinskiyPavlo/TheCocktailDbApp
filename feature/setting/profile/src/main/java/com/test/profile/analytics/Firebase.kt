package com.test.profile.analytics

import androidx.core.os.bundleOf
import com.google.firebase.analytics.FirebaseAnalytics
import com.test.firebase.common.Analytic

internal fun FirebaseAnalytics.logUserNameChanged(userFullName: String?) {
    logEvent(
        Analytic.PROFILE_DATA_CHANGE,
        bundleOf(
            Analytic.PROFILE_DATA_CHANGE_KEY to userFullName
        )
    )
}

internal fun FirebaseAnalytics.logUserAvatarChanged(newAvatarUrl: String?, userFullName: String?) {
    logEvent(
        Analytic.PROFILE_AVATAR_CHANGE,
        bundleOf(
            Analytic.PROFILE_AVATAR_CHANGE_AVATAR_KEY to newAvatarUrl,
            Analytic.PROFILE_AVATAR_CHANGE_NAME_KEY to userFullName
        )
    )
}
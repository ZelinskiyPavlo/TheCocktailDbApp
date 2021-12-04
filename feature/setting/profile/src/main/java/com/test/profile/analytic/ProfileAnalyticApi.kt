package com.test.profile.analytic

interface ProfileAnalyticApi {

    fun logUserNameChanged(userFullName: String)

    fun logUserAvatarChanged(newAvatarUrl: String, userFullName: String)

}
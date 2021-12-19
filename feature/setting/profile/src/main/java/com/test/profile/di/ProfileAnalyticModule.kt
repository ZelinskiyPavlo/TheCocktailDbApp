package com.test.profile.di

import com.test.profile.analytic.ProfileAnalyticApi
import com.test.profile.analytic.ProfileAnalyticImpl
import dagger.Binds
import dagger.Module

@Module
interface ProfileAnalyticModule {

    @Suppress("unused")
    @Binds
    fun bindProfileAnalytic(profileAnalyticsImpl: ProfileAnalyticImpl): ProfileAnalyticApi
}
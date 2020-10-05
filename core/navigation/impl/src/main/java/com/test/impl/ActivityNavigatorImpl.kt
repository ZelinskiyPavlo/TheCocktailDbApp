package com.test.impl

import android.content.Context
import android.content.Intent
//import com.test.auth.ui.AuthActivity
import com.test.navigation.souce.ActivityNavigator
import com.test.navigation.souce.ActivityStarter

internal class ActivityNavigatorImpl(
    private val context: Context,
    private val activityStarter: ActivityStarter
): ActivityNavigator {

    override fun openAuth() = Unit /*activityStarter.run {
        startActivity(Intent(context, AuthActivity::class.java))
    }*/

    override fun openMain() = Unit /*= activityStarter.run {
        startActivity(Intent(context, MainActivity::class.java))
    }*/
}
package com.test.thecocktaildb.navigation

//import android.content.Context
//import android.content.Intent
//import com.test.navigation.souce.ActivityNavigable
//import com.test.navigation.souce.ActivityStarter
//import com.test.thecocktaildb.presentation.ui.auth.AuthActivity
//import com.test.thecocktaildb.presentation.ui.cocktail.MainActivity
//
//internal class NavigatorImpl(
//    private val context: Context,
//    private val activityStarter: com.test.navigation.souce.ActivityStarter
//): com.test.navigation.souce.ActivityNavigable {
//
//    override fun openAuth() = activityStarter.run {
//        startActivity(Intent(context, AuthActivity::class.java))
//    }
//
//    override fun openMain() = activityStarter.run {
//        startActivity(Intent(context, MainActivity::class.java))
//    }
//}
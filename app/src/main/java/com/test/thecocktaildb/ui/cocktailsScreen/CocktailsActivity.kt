package com.test.thecocktaildb.ui.cocktailsScreen

import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import com.test.thecocktaildb.R
import com.test.thecocktaildb.data.Cocktail
import com.test.thecocktaildb.ui.base.BaseActivity
import com.test.thecocktaildb.ui.cocktailsScreen.callback.FragmentEventCallback
import com.test.thecocktaildb.ui.cocktailsScreen.callback.OnFavoriteClicked
import com.test.thecocktaildb.ui.cocktailsScreen.callback.OnFilterApplied
import com.test.thecocktaildb.ui.cocktailsScreen.drinkFilter.DrinkFilter
import com.test.thecocktaildb.ui.cocktailsScreen.drinkFilter.DrinkFilterType
import com.test.thecocktaildb.ui.cocktailsScreen.filterScreen.CocktailFilterFragment
import com.test.thecocktaildb.ui.cocktailsScreen.fragmentHostScreen.HostFragment
import com.test.thecocktaildb.util.receiver.AirplaneReceiver

class CocktailsActivity : BaseActivity(),
    FragmentEventCallback, OnFavoriteClicked {

    private lateinit var airplaneBroadcastReceiver: BroadcastReceiver

    private lateinit var cocktailFilterFragment: CocktailFilterFragment

    private var listeners: MutableSet<OnFilterApplied> = mutableSetOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    override fun onStart() {
        super.onStart()

        airplaneBroadcastReceiver =
            AirplaneReceiver()
        val intentFilter = IntentFilter().apply {
            addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED)
        }
        registerReceiver(airplaneBroadcastReceiver, intentFilter)
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(airplaneBroadcastReceiver)
    }

    override fun navigateToFilterFragmentEvent() {
        val drinkFilterTypeList = listOf(DrinkFilterType.ALCOHOL, DrinkFilterType.CATEGORY)

        cocktailFilterFragment = CocktailFilterFragment.newInstance(drinkFilterTypeList)
        supportFragmentManager.beginTransaction()
            .add(R.id.nav_host_fragment, cocktailFilterFragment)
            .addToBackStack(null)
            .commit()
    }

    override fun navigateToHostFragmentEvent(filterTypeList: List<DrinkFilter?>) {
        listeners.forEach { it.applyFilter(filterTypeList) }

        supportFragmentManager.popBackStack()
    }

    override fun resetFilterEvent() {
        listeners.forEach { it.resetFilter() }
    }

    override fun addCallback(listener: OnFilterApplied) {
        listeners.add(listener)
    }

    override fun removeCallback(listener: OnFilterApplied) {
        listeners.remove(listener)
    }

    override fun onFavoriteAdded(cocktail: Cocktail) {
        val navHost = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
        navHost?.let { navFragment ->
            navFragment.childFragmentManager.primaryNavigationFragment?.let { hostFragment ->
                hostFragment as HostFragment
                hostFragment.onFavoriteAdded(cocktail)
            }
        }
    }
}

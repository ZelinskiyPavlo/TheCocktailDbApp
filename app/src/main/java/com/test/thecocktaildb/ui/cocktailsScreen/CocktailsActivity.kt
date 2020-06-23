package com.test.thecocktaildb.ui.cocktailsScreen

import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import com.test.thecocktaildb.R
import com.test.thecocktaildb.ui.base.BaseActivity
import com.test.thecocktaildb.ui.cocktailsScreen.drinkFilter.DrinkFilter
import com.test.thecocktaildb.ui.cocktailsScreen.drinkFilter.DrinkFilterType
import com.test.thecocktaildb.util.receiver.AirplaneReceiver

class CocktailsActivity : BaseActivity(), FragmentNavigationListener {

    private lateinit var airplaneBroadcastReceiver: BroadcastReceiver

    private lateinit var cocktailFilterFragment: CocktailFilterFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(findViewById(R.id.toolBar))

        navController = findNavController(R.id.nav_host_fragment)

        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        showSearchFieldInSearchCocktailsFragment()
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

    override fun navigateToFilterFragment() {
        val drinkFilterTypeList = listOf(DrinkFilterType.ALCOHOL, DrinkFilterType.CATEGORY)

        cocktailFilterFragment = CocktailFilterFragment.newInstance(drinkFilterTypeList)
        supportFragmentManager.beginTransaction().replace(
            R.id.nav_host_fragment, cocktailFilterFragment, null
        ).addToBackStack(null).commit()
    }

    override fun navigateToCocktailFragment(filterTypeList: List<DrinkFilter?>) {
        supportFragmentManager.beginTransaction().remove(cocktailFilterFragment).runOnCommit {
            val navHost = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
            navHost?.let { navFragment ->
                navFragment.childFragmentManager.primaryNavigationFragment?.let { cocktailFragment ->
                    cocktailFragment as CocktailsFragment
                    cocktailFragment.applyFilter(filterTypeList)
                }
            }
        }.commit()

    }
}

package com.test.thecocktaildb.ui.cocktailsScreen

import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.test.thecocktaildb.R
import com.test.thecocktaildb.ui.base.BaseActivity
import com.test.thecocktaildb.util.AirplaneReceiver
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*

class CocktailsActivity : BaseActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration

    private lateinit var navController: NavController

    // This needs to be injected
    private lateinit var airplaneBroadcastReceiver: BroadcastReceiver

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

        val airplaneBroadcastReceiver = AirplaneReceiver()
        val intentFilter = IntentFilter().apply {
            addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED)
        }
        registerReceiver(airplaneBroadcastReceiver, intentFilter)
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(airplaneBroadcastReceiver)
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.nav_host_fragment)
            .navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun showSearchFieldInSearchCocktailsFragment() {
        fun changeBackButtonState(enabled: Boolean) {
            supportActionBar?.setDisplayHomeAsUpEnabled(enabled)
            supportActionBar?.setHomeButtonEnabled(enabled)
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.cocktailsFragment -> {
                    changeBackButtonState(false)
                    toolBar.search_field_layout.visibility = View.GONE
                    toolBar.cocktails_toolbar_layout.visibility = View.VISIBLE
                }
                R.id.searchCocktailsFragment -> {
                    changeBackButtonState(false)
                    toolBar.search_field_layout.visibility = View.VISIBLE
                    toolBar.cocktails_toolbar_layout.visibility = View.GONE
                }
                R.id.cocktailDetailsFragment -> {
                    changeBackButtonState(true)
                    toolBar.search_field_layout.visibility = View.GONE
                    toolBar.cocktails_toolbar_layout.visibility = View.GONE
                }
            }
        }
    }
}

package com.test.thecocktaildb.ui.cocktailsScreen

import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import com.test.thecocktaildb.R
import com.test.thecocktaildb.ui.base.BaseActivity
import com.test.thecocktaildb.util.receiver.AirplaneReceiver

class CocktailsActivity : BaseActivity() {

    private lateinit var airplaneBroadcastReceiver: BroadcastReceiver

    private lateinit var intentFilter: IntentFilter

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
}


package com.test.thecocktaildb.ui.cocktailsScreen

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import com.test.thecocktaildb.R
import com.test.thecocktaildb.ui.base.BaseActivity

//class CocktailsActivity : AppCompatActivity() {
class CocktailsActivity : BaseActivity() {

    private lateinit var batteryBroadcastReceiver: BroadcastReceiver

    private lateinit var intentFilter: IntentFilter

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()

        batteryBroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(p0: Context?, p1: Intent?) {
                val intentFilter: IntentFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
            }
        }
        val intentFilter = IntentFilter().apply {
            addAction(Intent.ACTION_BATTERY_OKAY)
            addAction(Intent.ACTION_BATTERY_LOW)
        }
        val batteryStatus =
            applicationContext.registerReceiver(batteryBroadcastReceiver, intentFilter)
    }
}

